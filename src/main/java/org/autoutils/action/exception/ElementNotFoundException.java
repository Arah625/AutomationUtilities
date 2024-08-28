package org.autoutils.action.exception;

/**
 * This exception is thrown when an element cannot be found within the specified timeout period during a swipe operation.
 * It typically occurs when the element is not visible on the screen or does not exist in the current view.
 *
 * <p>Usage Example:
 * <pre>
 * {@code
 * try {
 *     swipeHandler.swipeUntilElementFound(element, "up", Duration.ofSeconds(10));
 * } catch (ElementNotFoundException e) {
 *     // Handle the case where the element was not found
 *     logger.error("Element not found: " + e.getMessage());
 * }
 * }
 * </pre>
 */
public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
