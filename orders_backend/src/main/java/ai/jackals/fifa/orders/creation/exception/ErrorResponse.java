package ai.jackals.fifa.orders.creation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error payload returned by REST controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Timestamp when the error occurred */
    private LocalDateTime timestamp;

    /** HTTP status code */
    private int status;

    /** Short reason phrase, e.g. "Bad Request" */
    private String error;

    /** Human-readable error message */
    private String message;

    /** Request path that caused the error */
    private String path;

    /** Field / additional error details */
    private Map<String, Object> details;
}
