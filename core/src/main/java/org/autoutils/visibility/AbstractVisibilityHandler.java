package org.autoutils.visibility;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * AbstractVisibilityHandler provides a base class for handling visibility-related operations in Selenium WebDriver.
 * It encapsulates common functionality such as managing WebDriver instances and creating custom FluentWait instances.
 * Subclasses can extend this class to implement specific visibility-related behaviors, ensuring consistency and reusability.
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * public class ElementVisibilityHandler extends AbstractVisibilityHandler {
 *
 *     public ElementVisibilityHandler(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
 *         super(webDriver, webDriverWait, defaultFluentWait);
 *     }
 *
 *     // Custom visibility logic here
 * }
 * }</pre>
 */
abstract class AbstractVisibilityHandler {

    protected final WebDriver webDriver;
    protected final WebDriverWait webDriverWait;
    protected final FluentWait<WebDriver> defaultFluentWait;

    /**
     * Constructs a new AbstractVisibilityHandler with the provided WebDriver, WebDriverWait, and FluentWait instances.
     * Subclasses should call this constructor to initialize these common dependencies.
     *
     * @param webDriver        The WebDriver instance used for browser interaction.
     * @param webDriverWait    The WebDriverWait instance used for explicit waits.
     * @param defaultFluentWait The FluentWait instance used for custom waiting conditions.
     */
    protected AbstractVisibilityHandler(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
        this.defaultFluentWait = defaultFluentWait;
    }

    /**
     * Creates a custom FluentWait instance with specified timeout and polling interval.
     * This method is useful when you need to customize the waiting behavior for specific scenarios where the default settings are not sufficient.
     * It allows for fine-grained control over how long to wait for a condition to be met and how frequently to check for that condition.
     * The created FluentWait instance will ignore NoSuchElementException by default, making it resilient to transient issues where elements
     * may not be immediately present in the DOM.
     *
     * @param timeout         The maximum time to wait for a condition before timing out. This ensures that tests do not run indefinitely
     *                        and helps manage scenarios where an element may never become visible.
     * @param pollingInterval The interval at which to poll the condition. This controls how frequently the condition is checked, balancing
     *                        the responsiveness of the wait with the performance impact of frequent checks.
     * @return The FluentWait instance configured with the specified timeout and polling interval.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FluentWait<WebDriver> customWait = elementVisibilityHandler.createCustomFluentWait(Duration.ofSeconds(10), Duration.ofMillis(500));
     * boolean isElementVisible = customWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dynamicElement")));
     * }</pre>
     */
    protected FluentWait<WebDriver> createCustomFluentWait(Duration timeout, Duration pollingInterval) {
        return new FluentWait<>(webDriver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
    }
}
