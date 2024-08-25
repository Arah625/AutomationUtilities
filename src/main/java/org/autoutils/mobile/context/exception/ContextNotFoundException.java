package org.autoutils.mobile.context.exception;

/**
 * Exception thrown when the requested context is not found.
 * <p>
 * This exception is used to indicate that the context specified by the developer
 * does not exist or is not available in the current session.
 * </p>
 */
public class ContextNotFoundException extends RuntimeException {

    /**
     * Constructs a new ContextNotFoundException with the specified detail message.
     *
     * @param message the detail message, which provides more information about the cause of the exception.
     */
    public ContextNotFoundException(String message) {
        super(message);
    }

    public ContextNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
