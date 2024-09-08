package org.autoutils.driver.exception;

/**
 * Custom exception to handle invalid browser options passed to WebDriverFactory.
 */
public class InvalidBrowserOptionsException extends RuntimeException {

    public InvalidBrowserOptionsException(String message) {
        super(message);
    }
}
