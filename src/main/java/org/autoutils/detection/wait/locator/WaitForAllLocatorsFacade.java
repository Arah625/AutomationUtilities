package org.autoutils.detection.wait.locator;

import org.autoutils.detection.wait.AbstractWaitFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * The `WaitForAllLocatorsFacade` class provides utility methods for waiting until all elements identified
 * by a set of locators either become visible or invisible. This class is particularly useful in scenarios
 * where it is necessary to ensure that all elements identified by certain locators meet a specific condition
 * (visible or invisible) before proceeding with further actions in an automated test.
 *
 * <p>This class leverages Selenium's `FluentWait` and `WebDriverWait` to allow for fine-grained control over
 * the waiting behavior. The methods provided can handle both default and custom wait configurations,
 * offering flexibility in terms of timeout durations and polling intervals.</p>
 *
 * <p>The `WaitForAllLocatorsFacade` is part of a larger framework of facades designed to modularize
 * and simplify the handling of element visibility and presence checks in Selenium-based test automation.
 * It focuses on situations where multiple elements must all be in a certain state (visible or invisible)
 * before the test can proceed, making it ideal for testing complex UI flows where the presence or absence
 * of multiple elements is critical.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Instantiate the WaitForAllLocatorsFacade
 * WaitForAllLocatorsFacade waitForAllLocatorsFacade = new WaitForAllLocatorsFacade(driver, webDriverWait, fluentWait);
 *
 * // Wait for all form fields to become visible
 * boolean areFieldsVisible = waitForAllLocatorsFacade.areAllElementsByLocatorsVisible(fieldLocator1, fieldLocator2);
 *
 * // Wait for all loading indicators to disappear
 * boolean areIndicatorsGone = waitForAllLocatorsFacade.areAllElementsByLocatorsInvisible(loadingIndicatorLocator1, loadingIndicatorLocator2);
 * }</pre>
 *
 * <p>This class is typically used within higher-level handlers, such as `NewElementVisibilityHandler`,
 * which orchestrate multiple facades to provide a comprehensive API for managing element visibility
 * and presence in complex UI testing scenarios.</p>
 */
public class WaitForAllLocatorsFacade extends AbstractWaitFacade {
    private final WaitForAllLocators waitForAllLocators;

    public WaitForAllLocatorsFacade(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForAllLocators = new WaitForAllLocators(webDriver);
    }

    /**
     * Waits for all elements identified by the given locators to become visible using the default FluentWait.
     * This method ensures that all elements identified by the locators become visible within the specified wait period.
     *
     * @param locatorsList List of locators identifying the elements to check for visibility.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areFormFieldsVisible(List<By> fieldLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(fieldLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisible(List<By> locatorsList) {
        return waitForAllLocators.waitForVisibilityOfAllLocators(defaultFluentWait, locatorsList);
    }

    /**
     * Waits for all elements identified by the given locators to become visible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions for a group of elements.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    List of locators identifying the elements to check for visibility.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areLoadingIndicatorsVisible(List<By> loadingLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(Duration.ofSeconds(10), Duration.ofMillis(500), loadingLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForVisibilityOfAllLocators(fluentWait, locatorsList);
    }

    /**
     * Waits for all elements identified by the specified locators to become visible using the default FluentWait.
     * This method ensures that all elements identified by the locators become visible within the specified wait period.
     *
     * @param locators Locators identifying the elements to check for visibility.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areErrorMessagesVisible() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(errorLocator1, errorLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisible(By... locators) {
        return waitForAllLocators.waitForVisibilityOfAllLocators(defaultFluentWait, locators);
    }

    /**
     * Waits for all elements identified by the specified locators to become visible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions, ensuring that all specified elements become visible.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check for visibility.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areWarningsVisible() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(Duration.ofSeconds(5), Duration.ofMillis(500), warningLocator1, warningLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForVisibilityOfAllLocators(fluentWait, locators);
    }

    /**
     * Checks if all elements identified by the given locators are visible using the default FluentWait.
     * This method ensures that all elements identified by the locators become visible within the specified wait period.
     *
     * @param locatorsList List of locators identifying the elements to check.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areFormFieldsVisible(List<By> fieldLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(fieldLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisibleOnce(List<By> locatorsList) {
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(defaultFluentWait, locatorsList);
    }

    /**
     * Checks if all elements identified by the given locators are visible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions for a group of elements.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    List of locators identifying the elements to check.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areLoadingIndicatorsVisible(List<By> loadingLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(Duration.ofSeconds(10), Duration.ofMillis(500), loadingLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisibleOnce(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(fluentWait, locatorsList);
    }

    /**
     * Checks if elements identified by the given locators are all visible using the default FluentWait.
     * This method ensures that all elements identified by the locators are visible simultaneously within the specified wait period.
     *
     * @param locators Locators identifying the elements to check.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areErrorMessagesVisible() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(errorLocator1, errorLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisibleOnce(By... locators) {
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(defaultFluentWait, locators);
    }

    /**
     * Checks if elements identified by the given locators are all visible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions, ensuring that all specified elements become visible.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check.
     * @return True if all elements become visible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areWarningsVisible() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsVisible(Duration.ofSeconds(5), Duration.ofMillis(500), warningLocator1, warningLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsVisibleOnce(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(fluentWait, locators);
    }

    /**
     * Waits for all elements identified by the given locators to become invisible using the default FluentWait.
     * This method ensures that all elements identified by the locators become invisible within the specified wait period.
     *
     * @param locatorsList List of locators identifying the elements to check for invisibility.
     * @return True if all elements become invisible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areLoadingIndicatorsGone(List<By> loadingLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisible(loadingLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisible(List<By> locatorsList) {
        return waitForAllLocators.waitForInvisibilityOfAllLocators(defaultFluentWait, locatorsList);
    }

    /**
     * Waits for all elements identified by the given locators to become invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions for a group of elements.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    List of locators identifying the elements to check for invisibility.
     * @return True if all elements become invisible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAlertsGone(List<By> alertLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisible(Duration.ofSeconds(10), Duration.ofMillis(500), alertLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForInvisibilityOfAllLocators(fluentWait, locatorsList);
    }

    /**
     * Waits for all elements identified by the specified locators to become invisible using the default FluentWait.
     * This method ensures that all elements identified by the locators become invisible within the specified wait period.
     *
     * @param locators Locators identifying the elements to check for invisibility.
     * @return True if all elements become invisible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areErrorMessagesGone() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisible(errorLocator1, errorLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisible(By... locators) {
        return waitForAllLocators.waitForInvisibilityOfAllLocators(defaultFluentWait, locators);
    }

    /**
     * Waits for all elements identified by the specified locators to become invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions, ensuring that all specified elements become invisible.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check for invisibility.
     * @return True if all elements become invisible within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areTemporaryBannersGone() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisible(Duration.ofSeconds(5), Duration.ofMillis(500), bannerLocator1, bannerLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForInvisibilityOfAllLocators(fluentWait, locators);
    }

    /**
     * Checks if all elements identified by the given locators are invisible using the default FluentWait.
     * This method ensures that each element identified by the locators becomes invisible at least once within the specified wait period.
     *
     * @param locatorsList List of locators identifying the elements to check.
     * @return True if all elements become invisible at least once within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areFormWarningsGone(List<By> warningLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisibleOnce(warningLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisibleOnce(List<By> locatorsList) {
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(defaultFluentWait, locatorsList);
    }

    /**
     * Checks if all elements identified by the given locators are invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions, ensuring that each specified element becomes invisible at least once.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    List of locators identifying the elements to check.
     * @return True if all elements become invisible at least once within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean arePageIndicatorsCleared(List<By> indicatorLocators) {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisibleOnce(Duration.ofSeconds(10), Duration.ofMillis(500), indicatorLocators);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisibleOnce(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(fluentWait, locatorsList);
    }

    /**
     * Checks if all elements identified by the specified locators are invisible using the default FluentWait.
     * This method ensures that each element identified by the locators becomes invisible at least once within the specified wait period.
     *
     * @param locators Locators identifying the elements to check.
     * @return True if all elements become invisible at least once within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areBannerAdsCleared() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisibleOnce(adLocator1, adLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisibleOnce(By... locators) {
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(defaultFluentWait, locators);
    }

    /**
     * Checks if all elements identified by the specified locators are invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method allows for more granular control over the wait conditions, ensuring that each specified element becomes invisible at least once.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check.
     * @return True if all elements become invisible at least once within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllNotificationsGone() {
     *     return elementVisibilityHandler.areAllElementsByLocatorsInvisibleOnce(Duration.ofSeconds(5), Duration.ofMillis(500), notificationLocator1, notificationLocator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsByLocatorsInvisibleOnce(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(fluentWait, locators);
    }
}
