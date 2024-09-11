package org.autoutils.mobile.appiumserver.exception;

/**
 * Exception thrown when no available port is found within the specified range.
 */
public class NoAvailablePortException extends RuntimeException {

    /**
     * Constructs a new NoAvailablePortException with the specified detail message.
     *
     * @param message the detail message.
     */
    public NoAvailablePortException(String message) {
        super(message);
    }
}