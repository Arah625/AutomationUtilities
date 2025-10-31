package org.autoutils.action.exception;

/**
 * This exception is thrown when an error occurs during the execution of a swipe operation.
 * It can happen due to various reasons such as issues with the swipe gesture itself or problems within the Appium driver.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * try {
 *     swipeHandler.swipeInDirection("up");
 * } catch (SwipeExecutionException e) {
 *     // Handle the execution error
 *     logger.error("Swipe execution failed: " + e.getMessage());
 * }
 * }
 * </pre>
 */
public class SwipeExecutionException extends RuntimeException {
    public SwipeExecutionException(String message) {
        super(message);
    }

    public SwipeExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
