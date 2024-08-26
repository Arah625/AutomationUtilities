package org.autoutils.action.exception;

public class SwipeOperationException extends RuntimeException {
    public SwipeOperationException(String message) {
        super(message);
    }

    public SwipeOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}