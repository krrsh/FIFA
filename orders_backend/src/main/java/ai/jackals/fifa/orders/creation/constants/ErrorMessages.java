package ai.jackals.fifa.orders.creation.constants;

/**
 * Centralized error message constants used across REST and GraphQL layers.
 */
public final class ErrorMessages {
    private ErrorMessages() {}

    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String UNEXPECTED_ERROR = "Unexpected error";
    public static final String REFERENCED_RESOURCE_NOT_FOUND = "Referenced resource not found";
}
