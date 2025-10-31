package org.autoutils.visibility;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * The `LocatorInvisibility` class provides methods for checking the invisibility of web elements
 * identified by locators using various wait strategies. It is designed to ensure that specific elements
 * are not visible or present in the DOM, which is essential for validating UI conditions in dynamic web applications.
 */
public class LocatorInvisibility extends AbstractVisibilityHandler {

    private final WaitForAllLocators waitForAllLocators;
    private final WaitForFirstLocator waitForFirstLocator;

    /**
     * Constructs an instance of `LocatorInvisibility`.
     *
     * @param webDriver         The WebDriver instance to be used.
     * @param webDriverWait     The WebDriverWait instance to be used.
     * @param defaultFluentWait The FluentWait instance to be used.
     */
    protected LocatorInvisibility(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForAllLocators = new WaitForAllLocators(webDriver);
        this.waitForFirstLocator = new WaitForFirstLocator(webDriver);
    }

    /**
     * Checks for the invisibility of an element identified by a locator using the default WebDriverWait.
     * This method ensures that the element identified by the locator is no longer visible.
     *
     * @param locator The locator identifying the element.
     * @return True if the element is invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTooltipInvisible() {
     *     return elementVisibilityHandler.invisibilityOfElement(By.id("errorTooltip"));
     * }
     * }</pre>
     */
    public boolean isElementInvisible(By locator) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of an element identified by a locator within a specified timeout using a custom WebDriverWait.
     * This method is useful when ensuring that an element is no longer visible within a specific time frame.
     *
     * @param locator The locator identifying the element.
     * @param timeout The maximum duration to wait for the element's invisibility.
     * @return True if the element is invisible within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isNotificationBarInvisible() {
     *     return elementVisibilityHandler.invisibilityOfElement(By.id("notificationBar"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementInvisible(By locator, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of an element with exact text identified by a locator.
     * This method is useful when you need to ensure that an element with the specified text is no longer visible or present.
     *
     * @param locator The locator identifying the element.
     * @param text    The exact text to verify the invisibility of within the element identified by the locator.
     * @return True if the element with the exact text is invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTooltipWithExactTextInvisible(String errorText) {
     *     return elementVisibilityHandler.invisibilityOfElementWithExactText(By.id("errorTooltip"), errorText);
     * }
     * }</pre>
     */
    public boolean invisibilityOfElementWithExactText(By locator, String text) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of an element with exact text identified by a locator within a specified timeout.
     * This method is useful when you need to ensure that an element with the specified text is no longer visible or present within a specific timeframe.
     *
     * @param locator The locator identifying the element.
     * @param text    The exact text to verify the invisibility of within the element identified by the locator.
     * @param timeout The maximum duration to wait for the element's invisibility.
     * @return True if the element with the exact text is invisible within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTooltipWithExactTextInvisible(String errorText) {
     *     return elementVisibilityHandler.invisibilityOfElementWithExactText(By.id("errorTooltip"), errorText, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean invisibilityOfElementWithExactText(By locator, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
        } catch (TimeoutException exception) {
            return false;
        }
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
