package org.autoutils.driver.exception;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}