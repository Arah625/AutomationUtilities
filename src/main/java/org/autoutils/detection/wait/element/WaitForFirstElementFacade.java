package org.autoutils.detection.wait.element;

import org.autoutils.detection.wait.AbstractWaitFacade;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * The `WaitForFirstElementFacade` class provides utility methods to wait for the visibility or invisibility
 * of the first web element from a list of web elements. This class is particularly useful in scenarios where
 * the appearance or disappearance of any one element from a group is sufficient to proceed with the test or
 * interaction in automated UI testing.
 *
 * <p>This class leverages Selenium's `FluentWait` and `WebDriverWait` to provide fine-grained control over
 * the waiting behavior, allowing for custom timeouts and polling intervals. The methods in this class can
 * be used in complex test scenarios where the timing of element appearance or disappearance is critical
 * for test reliability and accuracy.</p>
 *
 * <p>The `WaitForFirstElementFacade` is part of a broader set of facades designed to modularize and simplify
 * the handling of element visibility checks in Selenium-based test automation. It focuses on situations
 * where only one of several elements needs to be in a certain state (visible or invisible) for the test
 * to proceed, making it ideal for testing dynamic and complex user interfaces.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Instantiate the WaitForFirstElementFacade
 * WaitForFirstElementFacade waitForFirstElementFacade = new WaitForFirstElementFacade(driver, webDriverWait, fluentWait);
 *
 * // Wait for any notification to become visible
 * boolean isNotificationVisible = waitForFirstElementFacade.isAnyElementVisible(notification1, notification2);
 *
 * // Wait for any error message to disappear
 * boolean isErrorHidden = waitForFirstElementFacade.isAnyElementInvisible(errorMessage1, errorMessage2);
 * }</pre>
 *
 * <p>This class is typically used within higher-level handlers, such as `NewElementVisibilityHandler`,
 * which orchestrate multiple facades to provide a comprehensive API for managing element visibility
 * in complex UI testing scenarios.</p>
 */
public class WaitForFirstElementFacade extends AbstractWaitFacade {

    private final WaitForFirstElement waitForFirstElement;
    public WaitForFirstElementFacade(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForFirstElement = new WaitForFirstElement(webDriver);
    }

    /**
     * Waits for any element from a list of web elements to become visible using the default FluentWait.
     * This method is useful for scenarios where only one of several elements needs to be visible for the test to proceed.
     *
     * @param webElementsList List of elements to check for visibility.
     * @return True if any of the elements in the list are visible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyFieldErrorVisible(List<WebElement> fieldErrors) {
     *     return elementVisibilityHandler.isAnyElementVisible(fieldErrors);
     * }
     * }</pre>
     */
    public boolean isAnyElementVisible(List<WebElement> webElementsList) {
        return waitForFirstElement.waitForVisibilityOfFirstElement(defaultFluentWait, webElementsList);
    }

    /**
     * Waits for any element from a list of web elements to become visible within a specified timeout and polling interval.
     * This method offers fine-grained control over the wait behavior, allowing for customized wait conditions.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of elements to check for visibility.
     * @return True if any of the elements in the list become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyMessageVisible(List<WebElement> messages) {
     *     return elementVisibilityHandler.isAnyElementVisible(Duration.ofSeconds(10), Duration.ofMillis(500), messages);
     * }
     * }</pre>
     */
    public boolean isAnyElementVisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstElement.waitForVisibilityOfFirstElement(fluentWait, webElementsList);
    }

    /**
     * Waits for any of the specified web elements to become visible using the default FluentWait.
     * This method returns true as soon as the first element from the list is visible, allowing for early exit.
     * This is useful in scenarios where any one of multiple possible elements indicates readiness to proceed.
     *
     * @param webElements Elements to check for visibility.
     * @return True if any of the elements are visible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyNotificationVisible() {
     *     return elementVisibilityHandler.isAnyElementVisible(notification1, notification2);
     * }
     * }</pre>
     */
    public boolean isAnyElementVisible(WebElement... webElements) {
        return waitForFirstElement.waitForVisibilityOfFirstElement(defaultFluentWait, webElements);
    }

    /**
     * Waits for any of the specified web elements to become visible within a custom timeout and polling interval.
     * This method returns as soon as the first visible element is found, optimizing test performance by avoiding unnecessary waits.
     * This is particularly useful in cases where the visibility of any one element is sufficient for the test to proceed.
     *
     * @param timeout         The maximum time to wait for any element to become visible.
     * @param pollingInterval The interval to poll for element visibility.
     * @param webElements     Varargs of web elements to check for visibility.
     * @return True if any element becomes visible within the specified wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyBannerVisible() {
     *     return elementVisibilityHandler.isAnyElementVisible(Duration.ofSeconds(10), Duration.ofMillis(500), banner1, banner2);
     * }
     * }</pre>
     */
    public boolean isAnyElementVisible(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstElement.waitForVisibilityOfFirstElement(fluentWait, webElements);
    }

    /**
     * Waits for any element from a list of web elements to become invisible using the default FluentWait.
     * This method is useful for scenarios where the disappearance of any one of several elements indicates readiness to proceed.
     *
     * @param webElementsList List of elements to check for invisibility.
     * @return True if any of the elements in the list become invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyErrorHidden(List<WebElement> errorMessages) {
     *     return elementVisibilityHandler.isAnyElementInvisible(errorMessages);
     * }
     * }</pre>
     */
    public boolean isAnyElementInvisible(List<WebElement> webElementsList) {
        return waitForFirstElement.waitForInvisibilityOfFirstElement(defaultFluentWait, webElementsList);
    }

    /**
     * Waits for any element from a list of web elements to become invisible within a specified timeout and polling interval.
     * This method offers fine-grained control over the wait behavior, allowing for customized wait conditions.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of elements to check for invisibility.
     * @return True if any of the elements in the list become invisible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyNotificationHidden(List<WebElement> notifications) {
     *     return elementVisibilityHandler.isAnyElementInvisible(Duration.ofSeconds(10), Duration.ofMillis(500), notifications);
     * }
     * }</pre>
     */
    public boolean isAnyElementInvisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstElement.waitForInvisibilityOfFirstElement(fluentWait, webElementsList);
    }

    /**
     * Waits for any of the specified web elements to become invisible using the default FluentWait.
     * This method returns true as soon as the first element from the list becomes invisible, allowing for early exit.
     * This is useful in scenarios where any one of multiple possible elements disappearing indicates readiness to proceed.
     *
     * @param webElement Elements to check for invisibility.
     * @return True if any of the elements become invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyBannerHidden() {
     *     return elementVisibilityHandler.isAnyElementInvisible(banner1, banner2);
     * }
     * }</pre>
     */
    public boolean isAnyElementInvisible(WebElement... webElement) {
        return waitForFirstElement.waitForInvisibilityOfFirstElement(defaultFluentWait, webElement);
    }

    /**
     * Waits for any of the specified web elements to become invisible within a custom timeout and polling interval.
     * This method returns as soon as the first invisible element is found, optimizing test performance by avoiding unnecessary waits.
     * This is particularly useful in cases where the disappearance of any one element is sufficient for the test to proceed.
     *
     * @param timeout         The maximum time to wait for any element to become invisible.
     * @param pollingInterval The interval to poll for element invisibility.
     * @param webElement      Varargs of web elements to check for invisibility.
     * @return True if any element becomes invisible within the specified wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isAnyAlertHidden() {
     *     return elementVisibilityHandler.isAnyElementInvisible(Duration.ofSeconds(10), Duration.ofMillis(500), alert1, alert2);
     * }
     * }</pre>
     */
    public boolean isAnyElementInvisible(Duration timeout, Duration pollingInterval, WebElement... webElement) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstElement.waitForInvisibilityOfFirstElement(fluentWait, webElement);
    }
}
