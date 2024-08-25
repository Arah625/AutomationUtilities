package org.autoutils.wait.exception;

/**
 * Custom exception class that represents a timeout during a wait operation.
 * This exception is thrown when a specified condition is not met within the configured timeout period.
 */
public class WaitTimeoutException extends RuntimeException {

    /**
     * Constructs a new WaitTimeoutException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *                A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public WaitTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
