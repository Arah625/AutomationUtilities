package org.autoutils.retry.exceptions;

/**
 * Represents exceptions that occur when handling other exceptions within the application.
 * This class extends {@link RuntimeException} to provide a way to wrap and rethrow exceptions
 * with additional context or information, facilitating better error management and debugging.
 */
public class ExceptionCaughtException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public ExceptionCaughtException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method). A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or unknown.
     */
    public ExceptionCaughtException(String message, Throwable cause) {
        super(message, cause);
    }
}