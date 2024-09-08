package org.autoutils.driver.exception;

/**
 * Custom exception to handle unsupported or invalid browsers passed to WebDriverFactory.
 */
public class InvalidBrowserException extends RuntimeException {

    public InvalidBrowserException(String message) {
        super(message);
    }
}
