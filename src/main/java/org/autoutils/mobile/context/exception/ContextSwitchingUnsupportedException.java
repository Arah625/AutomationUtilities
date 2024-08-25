package org.autoutils.mobile.context.exception;

/**
 * Exception thrown when the driver does not support context switching.
 * <p>
 * This exception is used to indicate that an attempt to switch contexts has failed
 * because the driver in use does not implement the necessary capabilities for context switching.
 * </p>
 */
public class ContextSwitchingUnsupportedException extends RuntimeException {

    /**
     * Constructs a new ContextSwitchingNotSupportedException with the specified detail message.
     *
     * @param message the detail message, which provides more information about the cause of the exception.
     */
    public ContextSwitchingUnsupportedException(String message) {
        super(message);
    }

    public ContextSwitchingUnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
