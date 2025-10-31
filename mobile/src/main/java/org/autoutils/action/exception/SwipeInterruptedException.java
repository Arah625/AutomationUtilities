package org.autoutils.action.exception;

/**
 * This exception is thrown when a swipe operation is interrupted, typically due to an unexpected interruption of the thread performing the swipe.
 * This might occur if the app under test crashes or if there is a forced stop in the automation process.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * try {
 *     swipeHandler.swipeUntilElementFound(element, "up", Duration.ofSeconds(10));
 * } catch (SwipeInterruptedException e) {
 *     // Handle the interruption
 *     logger.error("Swipe operation was interrupted: " + e.getMessage());
 * }
 * }
 * </pre>
 */
public class SwipeInterruptedException extends RuntimeException {

    public SwipeInterruptedException(String message) {
        super(message);
    }

    public SwipeInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
