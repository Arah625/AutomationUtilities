package org.autoutils.retry.exception;

/**
 * Signals that an operation has exceeded the allowed number of repetitions. This runtime exception
 * is thrown to prevent infinite loops or excessive usage of resources by enforcing a maximum limit
 * on the number of times an action can be repeated.
 * <p>
 * This exception can be used in scenarios where there is a need to impose strict boundaries on
 * repetitive actions to ensure system stability and prevent potential abuse.
 */
public class MaximumRetriesExceededException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message. The message helps to provide
     * context about the nature of the error, specifically how and why the repetition limit was exceeded.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public MaximumRetriesExceededException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause. This constructor
     * is useful for wrapping lower-level exceptions that led to the violation of the repetition limit.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The cause of the exception. This can be used to trace back to the original error
     *                that resulted in exceeding the maximum number of repetitions.
     */
    public MaximumRetriesExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}