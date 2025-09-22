package ai.jackals.fifa.orders.creation.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import ai.jackals.fifa.orders.creation.constants.ErrorMessages;

/**
 * Centralized REST exception handling across all controllers.
 */
@RestControllerAdvice(basePackages = "ai.jackals.fifa.orders.creation")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /* --- Spring validation errors ( @Valid ) --- */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        String firstMessage = fieldErrors.values().stream().findFirst().orElse(ErrorMessages.VALIDATION_FAILED).toString();
        HttpStatus httpStatus = HttpStatus.valueOf(status.value());
        ErrorResponse body = buildError(httpStatus, firstMessage, request.getDescription(false), fieldErrors);
        return new ResponseEntity<>(body, httpStatus);
    }

    /* --- Bean validation on path/param etc. --- */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), null);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /* --- Custom app exceptions --- */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false), null);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false), null);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ---------------- private helpers ---------------- */
    private ErrorResponse buildError(HttpStatus status, String message, String path, Map<String, Object> details) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .details(details)
                .build();
    }
}
