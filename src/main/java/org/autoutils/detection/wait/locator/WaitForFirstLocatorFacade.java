package org.autoutils.detection.wait.locator;

import org.autoutils.detection.wait.AbstractWaitFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * The `WaitForFirstLocatorFacade` class provides utility methods to wait for the visibility or invisibility
 * of the first web element identified by one or more locators. It is particularly useful in scenarios where
 * the presence of any one element from a group of potential elements is sufficient to proceed with the test
 * or where the disappearance of any element from a set is required before continuing.
 *
 * <p>This class leverages Selenium's `FluentWait` and `WebDriverWait` to provide fine-grained control over
 * the waiting behavior, allowing for custom timeouts and polling intervals. The methods in this class can
 * be used in complex test scenarios where the timing of element appearance or disappearance is critical
 * for test reliability and accuracy.</p>
 *
 * <p>The methods in this facade are designed to be easy to use and flexible, supporting both default
 * waiting conditions and custom configurations. This facade is part of a broader set of facades aimed
 * at modularizing and simplifying the handling of element visibility checks in Selenium-based test automation.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Instantiate the WaitForFirstLocatorFacade
 * WaitForFirstLocatorFacade waitForFirstLocatorFacade = new WaitForFirstLocatorFacade(driver, webDriverWait, fluentWait);
 *
 * // Wait for any alert to become visible
 * boolean isVisible = waitForFirstLocatorFacade.isAnyElementByLocatorVisible(alertLocator1, alertLocator2);
 *
 * // Wait for any loading spinner to disappear
 * boolean isInvisible = waitForFirstLocatorFacade.isAnyElementByLocatorInvisible(spinnerLocator1, spinnerLocator2);
 * }</pre>
 *
 * <p>This class is typically used within higher-level handlers, such as `NewElementVisibilityHandler`,
 * which orchestrate multiple facades to provide a comprehensive API for managing element visibility
 * in complex UI testing scenarios.</p>
 */
public class WaitForFirstLocatorFacade extends AbstractWaitFacade {

    private final WaitForFirstLocator waitForFirstLocator;

    public WaitForFirstLocatorFacade(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForFirstLocator = new WaitForFirstLocator(webDriver);
    }

    /**
     * Checks if any element identified by locators in a list is visible using the default FluentWait.
     * This method is useful for scenarios where the visibility of any one of several elements is sufficient to proceed.
     *
     * @param locatorsList A list of locators identifying the elements to check.
     * @return True if any of the elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyProgressIndicatorVisible(List<By> progressLocators) {
     *     return elementVisibilityHandler.isAnyElementByLocatorVisible(progressLocators);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorVisible(List<By> locatorsList) {
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(defaultFluentWait, locatorsList);
    }

    /**
     * Checks if any element identified by locators in a list is visible using a custom FluentWait with a specified timeout and polling interval.
     * This method provides fine-grained control over the waiting conditions, making it suitable for complex test scenarios.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    A list of locators identifying the elements to check.
     * @return True if any of the elements become visible within the specified polling interval; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyFieldErrorVisible(List<By> fieldErrorLocators) {
     *     return elementVisibilityHandler.isAnyElementByLocatorVisible(Duration.ofSeconds(10), Duration.ofMillis(500), fieldErrorLocators);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorVisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(fluentWait, locatorsList);
    }

    /**
     * Checks if any element identified by the given locators is visible using the default FluentWait.
     * This method returns true as soon as the first element identified by the locators becomes visible, allowing for early exit.
     *
     * @param locators Locators identifying the elements to check.
     * @return True if any of the elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyAlertVisible() {
     *     return elementVisibilityHandler.isAnyElementByLocatorVisible(alertLocator1, alertLocator2);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorVisible(By... locators) {
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(defaultFluentWait, locators);
    }

    /**
     * Checks if any element identified by the given locators is visible using a custom FluentWait with a specified timeout and polling interval.
     * This method is useful when waiting for any one of multiple elements to become visible.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check.
     * @return True if any of the elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyNotificationVisible() {
     *     return elementVisibilityHandler.isAnyElementByLocatorVisible(Duration.ofSeconds(10), Duration.ofMillis(500), notificationLocator1, notificationLocator2);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorVisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(fluentWait, locators);
    }

    /**
     * Waits for any of the specified elements identified by the given locators to become invisible using the default FluentWait.
     * This method returns true as soon as the first element from the list becomes invisible, allowing for early exit.
     * It's particularly useful in scenarios where you need to ensure that any of several potential elements has disappeared from the UI.
     *
     * @param locators Varargs of locators identifying the elements to check for invisibility.
     * @return True if any of the elements identified by the locators become invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyErrorNotificationInvisible() {
     *     return elementVisibilityHandler.isAnyElementByLocatorInvisible(errorNotificationLocator1, errorNotificationLocator2);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorInvisible(By... locators) {
        return waitForFirstLocator.waitForInvisibilityOfFirstLocator(defaultFluentWait, locators);
    }

    /**
     * Waits for any of the specified elements identified by the given locators to become invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method is useful when you need to ensure that at least one element from a set of elements has disappeared within a specific time frame.
     * It optimizes test performance by avoiding unnecessary waits once an element becomes invisible.
     *
     * @param timeout         The maximum time to wait for any element to become invisible.
     * @param pollingInterval The interval to poll for element invisibility.
     * @param locators        Varargs of locators identifying the elements to check for invisibility.
     * @return True if any element identified by the locators becomes invisible within the specified wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyPopupInvisible() {
     *     return elementVisibilityHandler.isAnyElementByLocatorInvisible(Duration.ofSeconds(10), Duration.ofMillis(500), popupLocator1, popupLocator2);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorInvisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstLocator.waitForInvisibilityOfFirstLocator(fluentWait, locators);
    }

    /**
     * Waits for any element identified by locators in the provided list to become invisible using the default FluentWait.
     * This method is useful for scenarios where the disappearance of any element from a set indicates a condition met in your test.
     *
     * @param locatorsList A list of locators identifying the elements to check for invisibility.
     * @return True if any of the elements identified by the locators in the list become invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyLoadingSpinnerInvisible(List<By> spinnerLocators) {
     *     return elementVisibilityHandler.isAnyElementByLocatorInvisible(spinnerLocators);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorInvisible(List<By> locatorsList) {
        return waitForFirstLocator.waitForInvisibilityOfFirstLocator(defaultFluentWait, locatorsList);
    }

    /**
     * Waits for any element identified by locators in the provided list to become invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows you to specify custom wait times and polling intervals, making it ideal for complex scenarios where the timing of element disappearance is critical.
     *
     * @param timeout         The maximum time to wait for any element to become invisible.
     * @param pollingInterval The interval to poll for element invisibility.
     * @param locatorsList    A list of locators identifying the elements to check for invisibility.
     * @return True if any of the elements identified by the locators in the list become invisible within the specified wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyModalDialogInvisible(List<By> dialogLocators) {
     *     return elementVisibilityHandler.isAnyElementByLocatorInvisible(Duration.ofSeconds(10), Duration.ofMillis(500), dialogLocators);
     * }
     * }</pre>
     */
    public boolean isAnyElementByLocatorInvisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstLocator.waitForInvisibilityOfFirstLocator(fluentWait, locatorsList);
    }
}
