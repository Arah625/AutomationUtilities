package org.autoutils.visibility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * The `ElementInvisibility` class provides methods for checking the invisibility of web elements
 * using various wait strategies. It offers fine-grained control over how and when elements
 * should be considered invisible, making it ideal for dynamic and asynchronous web applications.
 */
public class ElementInvisibility extends AbstractVisibilityHandler {

    private final WaitForAllElements waitForAllElements;
    private final WaitForFirstElement waitForFirstElement;

    /**
     * Constructs an instance of `ElementInvisibility`.
     *
     * @param webDriver         The WebDriver instance to be used.
     * @param webDriverWait     The WebDriverWait instance to be used.
     * @param defaultFluentWait The FluentWait instance to be used.
     */
    protected ElementInvisibility(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForAllElements = new WaitForAllElements(webDriver);
        this.waitForFirstElement = new WaitForFirstElement(webDriver);
    }

    /**
     * Checks for the invisibility of a specific web element using the default WebDriverWait.
     * This method is useful when you need to ensure that an element is no longer visible before proceeding.
     *
     * @param webElement The WebElement to check for invisibility.
     * @return True if the element is invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isLoadingSpinnerInvisible() {
     *     return elementVisibilityHandler.invisibilityOfElement(loadingSpinner);
     * }
     * }</pre>
     */
    public boolean isElementInvisible(WebElement webElement) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOf(webElement));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of a specific web element within a specified timeout using a custom WebDriverWait.
     * This method allows for more precise control over the wait time and ensures the element is no longer visible.
     *
     * @param webElement The WebElement to check for invisibility.
     * @param timeout    The maximum duration to wait for the element's invisibility.
     * @return True if the element is invisible within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isPopupDialogInvisible() {
     *     return elementVisibilityHandler.invisibilityOfElement(popupDialog, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementInvisible(WebElement webElement, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.invisibilityOf(webElement));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of an element with exact text in a specific WebElement.
     * This method is useful when you need to ensure that a specific element with the exact text is no longer visible or present.
     *
     * @param element The WebElement to check for invisibility.
     * @param text    The exact text to verify the invisibility of within the WebElement.
     * @return True if the WebElement with the exact text is invisible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isNotificationWithExactTextInvisible(String notificationText) {
     *     return elementVisibilityHandler.invisibilityOfElementWithExactText(notificationElement, notificationText);
     * }
     * }</pre>
     */
    public boolean invisibilityOfElementWithExactText(WebElement element, String text) {
        try {
            return webDriverWait.until(driver -> {
                try {
                    return !element.getText().equals(text);
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    // Returns true because either the element is not present in the DOM or it is stale (no longer visible).
                    return true;
                }
            });
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of an element with exact text in a specific WebElement within a specified timeout.
     * This method is useful when you need to ensure that a specific element with the exact text is no longer visible or present within a specific timeframe.
     *
     * @param element The WebElement to check for invisibility.
     * @param text    The exact text to verify the invisibility of within the WebElement.
     * @param timeout The maximum duration to wait for the element's invisibility.
     * @return True if the WebElement with the exact text is invisible within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isNotificationWithExactTextInvisible(String notificationText) {
     *     return elementVisibilityHandler.invisibilityOfElementWithExactText(notificationElement, notificationText, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean invisibilityOfElementWithExactText(WebElement element, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(driver -> {
                try {
                    return !element.getText().equals(text);
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    // Returns true because either the element is not present in the DOM or it is stale (no longer visible).
                    return true;
                }
            });
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Waits for all web elements in the provided list to become invisible using the default FluentWait.
     * This method is useful for checking the invisibility of a collection of elements that must all be invisible at the same time.
     *
     * @param webElementList The list of web elements to check.
     * @return True if all elements in the list are invisible at the same time, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areWarningMessagesInvisible(List<WebElement> warningMessages) {
     *     return elementVisibilityHandler.areAllElementsInvisible(warningMessages);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisible(List<WebElement> webElementList) {
        return waitForAllElements.waitForInvisibilityOfAllElements(defaultFluentWait, webElementList);
    }

    /**
     * Waits for all web elements in the provided list to become invisible using a custom FluentWait with a specified timeout and polling interval.
     * This method is useful for cases where specific wait conditions need to be applied to a group of elements to ensure they all become invisible.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementList  List of web elements to check for invisibility.
     * @return True if all elements become invisible at the same time within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllLoadingIndicatorsInvisible(List<WebElement> loadingIndicators) {
     *     return elementVisibilityHandler.areAllElementsInvisible(Duration.ofSeconds(10), Duration.ofMillis(500), loadingIndicators);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForInvisibilityOfAllElements(fluentWait, webElementList);
    }

    /**
     * Waits for each of the specified web elements to become invisible using the default FluentWait.
     * This method is useful for ensuring that each element becomes invisible at least once during the wait period.
     *
     * @param webElements The web elements to check.
     * @return True if each element becomes invisible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean arePageElementsInvisible() {
     *     return elementVisibilityHandler.areAllElementsInvisible(footer, header, sidebar);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisible(WebElement... webElements) {
        return waitForAllElements.waitForInvisibilityOfAllElements(defaultFluentWait, webElements);
    }

    /**
     * Waits for each of the specified web elements to become invisible within a specified timeout and polling interval.
     * This method is useful when you need more control over the wait time and polling frequency to ensure each element becomes invisible.
     *
     * @param timeout         The maximum time to wait for the elements to become invisible.
     * @param pollingInterval The interval to poll for element invisibility.
     * @param webElements     The web elements to check.
     * @return True if each element becomes invisible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areTemporaryMessagesInvisible() {
     *     return elementVisibilityHandler.areAllElementsInvisible(Duration.ofSeconds(5), Duration.ofMillis(500), tempMessage1, tempMessage2);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisible(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForInvisibilityOfAllElements(fluentWait, webElements);
    }

    /**
     * Waits for each element in the provided list to become invisible at least once using the default FluentWait.
     * This method is useful for ensuring that each element in the list becomes invisible at least once during the wait period.
     *
     * @param webElementList The list of web elements to check.
     * @return True if each element in the list becomes invisible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllErrorMessagesGone(List<WebElement> errorMessages) {
     *     return elementVisibilityHandler.areAllElementsInvisibleOnce(errorMessages);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisibleOnce(List<WebElement> webElementList) {
        return waitForAllElements.waitForEachElementToBeInvisibleOnce(defaultFluentWait, webElementList);
    }

    /**
     * Waits for each element in the provided list to become invisible at least once using a custom FluentWait with a specified timeout and polling interval.
     * This method is useful for cases where specific wait conditions need to be applied to ensure each element is invisible at least once.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementList  List of web elements to check for invisibility.
     * @return True if each element in the list becomes invisible at least once within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areDismissedMessagesGone(List<WebElement> dismissedMessages) {
     *     return elementVisibilityHandler.areAllElementsInvisibleOnce(Duration.ofSeconds(10), Duration.ofMillis(500), dismissedMessages);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisibleOnce(Duration timeout, Duration pollingInterval, List<WebElement> webElementList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeInvisibleOnce(fluentWait, webElementList);
    }

    /**
     * Waits for each of the specified web elements to become invisible at least once using the default FluentWait.
     * This method is useful for ensuring that each element becomes invisible at least once during the wait period.
     *
     * @param webElements The web elements to check.
     * @return True if each element becomes invisible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areTemporaryNotificationsGone() {
     *     return elementVisibilityHandler.areAllElementsInvisibleOnce(notification1, notification2, notification3);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisibleOnce(WebElement... webElements) {
        return waitForAllElements.waitForEachElementToBeInvisibleOnce(defaultFluentWait, webElements);
    }

    /**
     * Waits for each of the specified web elements to become invisible at least once within a specified timeout and polling interval.
     * This method is useful when you need more control over the wait time and polling frequency to ensure each element becomes invisible.
     *
     * @param timeout         The maximum time to wait for the elements to become invisible.
     * @param pollingInterval The interval to poll for element invisibility.
     * @param webElements     The web elements to check.
     * @return True if each element becomes invisible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areStatusIndicatorsCleared() {
     *     return elementVisibilityHandler.areAllElementsInvisibleOnce(Duration.ofSeconds(5), Duration.ofMillis(500), indicator1, indicator2);
     * }
     * }</pre>
     */
    public boolean areAllElementsInvisibleOnce(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeInvisibleOnce(fluentWait, webElements);
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
