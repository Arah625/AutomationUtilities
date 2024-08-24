package org.autoutils.retry.exceptions;

/**
 * Signifies that an anticipated exception was not handled during execution. This class extends
 * {@link RuntimeException} to facilitate reporting unexpected execution paths where exceptions
 * that were expected to be caught and handled were not encountered.
 * <p>
 * This exception type can be useful for signaling a discrepancy between expected and actual
 * behavior in code blocks specifically designed to handle certain types of exceptions.
 */
public class ExceptionNotHandledException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public ExceptionNotHandledException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method). A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or unknown.
     */
    public ExceptionNotHandledException(String message, Throwable cause) {
        super(message, cause);
    }
}