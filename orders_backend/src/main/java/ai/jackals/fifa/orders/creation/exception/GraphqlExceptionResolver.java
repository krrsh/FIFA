package ai.jackals.fifa.orders.creation.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import ai.jackals.fifa.orders.creation.constants.ErrorMessages;
import ai.jackals.fifa.orders.creation.constants.GraphqlExtensionKeys;

/**
 * Converts backend exceptions into GraphQL-compliant error objects.
 */
@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        HttpStatus status = (ex instanceof ResourceNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;

        return buildError(status, extractFriendlyMessage(ex), env.getField().getName());
    }

    private String extractFriendlyMessage(Throwable ex) {
        String msg = ex.getMessage();
        if (msg == null) {
            return ErrorMessages.UNEXPECTED_ERROR;
        }
        // Foreign-key violation
        if (msg.contains("violates foreign key constraint")) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\((\\w+?)\\)=\\((.+?)\\)").matcher(msg);
            if (m.find()) {
                return String.format("%s %s not found", m.group(1), m.group(2));
            }
            return ErrorMessages.REFERENCED_RESOURCE_NOT_FOUND;
        }
        // Default to first sentence to avoid verbose SQL/stack traces
        int idx = msg.indexOf('\n');
        return idx > 0 ? msg.substring(0, idx) : msg;
    }

    private GraphQLError buildError(HttpStatus status, String message, String path) {
        return GraphqlErrorBuilder.newError()
                .errorType(status == HttpStatus.NOT_FOUND ? graphql.ErrorType.DataFetchingException : graphql.ErrorType.ExecutionAborted)
                .message(message)
                .extensions(Map.of(
                        GraphqlExtensionKeys.TIMESTAMP, LocalDateTime.now().toString(),
                        GraphqlExtensionKeys.STATUS, status.value(),
                        GraphqlExtensionKeys.ERROR, status.getReasonPhrase(),
                        GraphqlExtensionKeys.PATH, path))
                .build();
    }
}
