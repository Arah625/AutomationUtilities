package org.autoutils.detection.wait.element;

import org.autoutils.detection.wait.AbstractWaitFacade;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * The `WaitForAllElementsFacade` class provides utility methods to manage the visibility and invisibility of
 * multiple web elements in Selenium-based automated tests. It focuses on scenarios where the visibility or
 * invisibility of all elements in a given list or set of web elements is required before proceeding with test
 * execution. This is particularly useful in complex user interfaces where multiple elements must be present
 * or absent simultaneously.
 *
 * <p>This class utilizes Selenium's `FluentWait` and `WebDriverWait` to allow for flexible and customizable
 * wait conditions, offering both default and custom configurations for timeout and polling intervals.
 * By using this class, testers can ensure that all elements in a specified group meet the required visibility
 * conditions, which is critical for ensuring the reliability and stability of automated UI tests.</p>
 *
 * <p>The `WaitForAllElementsFacade` is designed to integrate seamlessly into larger test automation frameworks,
 * serving as a specialized component for handling groups of elements. It is part of a modular system that
 * breaks down complex waiting logic into reusable, focused classes.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Instantiate the WaitForAllElementsFacade
 * WaitForAllElementsFacade waitForAllElementsFacade = new WaitForAllElementsFacade(driver, webDriverWait, fluentWait);
 *
 * // Wait for all fields to become visible
 * boolean areFieldsVisible = waitForAllElementsFacade.areAllElementsVisible(field1, field2, field3);
 *
 * // Wait for all error messages to disappear
 * boolean areErrorsGone = waitForAllElementsFacade.areAllElementsInvisible(errorMessage1, errorMessage2);
 * }</pre>
 *
 * <p>This class is typically used within higher-level handlers, such as `NewElementVisibilityHandler`,
 * which coordinate multiple facades to provide a comprehensive API for managing element visibility
 * in dynamic and complex web applications.</p>
 */
public class WaitForAllElementsFacade extends AbstractWaitFacade {

    private final WaitForAllElements waitForAllElements;
    public WaitForAllElementsFacade(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForAllElements = new WaitForAllElements(webDriver);
    }

    /**
     * Waits for all web elements in the provided list to become visible using the default FluentWait.
     * This method is useful for checking the visibility of a collection of elements that must all be visible at the same time.
     *
     * @param webElementsList The list of web elements to check.
     * @return True if all elements in the list are visible at the same time, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean arePasswordRequirementsVisible(List<WebElement> passwordHints) {
     *     return elementVisibilityHandler.areAllElementsVisible(passwordHints);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisible(List<WebElement> webElementsList) {
        return waitForAllElements.waitForVisibilityOfAllElements(defaultFluentWait, webElementsList);
    }

    /**
     * Waits for all web elements in the provided list to become visible using a custom FluentWait with a specified timeout and polling interval.
     * This method is useful for cases where specific wait conditions need to be applied to a group of elements.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of web elements to check for visibility.
     * @return True if all elements become visible at the same time within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areMandatoryFieldsVisible(List<WebElement> mandatoryFields) {
     *     return elementVisibilityHandler.areAllElementsVisible(Duration.ofSeconds(10), Duration.ofMillis(500), mandatoryFields);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForVisibilityOfAllElements(fluentWait, webElementsList);
    }

    /**
     * Waits for all specified web elements to become visible using the default FluentWait.
     * This method ensures that all elements are visible simultaneously within the specified wait period.
     *
     * @param webElements The web elements to check.
     * @return True if all elements are visible at the same time, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areLoginFieldsVisible() {
     *     return elementVisibilityHandler.areAllElementsVisible(emailField, passwordField, loginButton);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisible(WebElement... webElements) {
        return waitForAllElements.waitForVisibilityOfAllElements(defaultFluentWait, webElements);
    }

    /**
     * Waits for all specified web elements to become visible within a specified timeout and polling interval.
     * This method is useful when you need more control over the wait time and polling frequency.
     *
     * @param timeout         The maximum time to wait for the elements to become visible.
     * @param pollingInterval The interval to poll for element visibility.
     * @param webElements     The web elements to check.
     * @return True if all elements are visible at the same time, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areImportantFieldsVisible() {
     *     return elementVisibilityHandler.areAllElementsVisible(Duration.ofSeconds(5), Duration.ofMillis(500), importantField1, importantField2);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisible(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForVisibilityOfAllElements(fluentWait, webElements);
    }

    /**
     * Waits for each element in the provided list to become visible at least once using the default FluentWait.
     * This method is useful for ensuring that each element in the list is visible at least once during the wait period.
     *
     * @param webElementsList The list of web elements to check.
     * @return True if each element in the list becomes visible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllMessagesDisplayedOnce(List<WebElement> messages) {
     *     return elementVisibilityHandler.areAllElementsVisibleOnce(messages);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisibleOnce(List<WebElement> webElementsList) {
        return waitForAllElements.waitForEachElementToBeVisibleOnce(defaultFluentWait, webElementsList);
    }

    /**
     * Waits for each element in the provided list to become visible at least once using a custom FluentWait with a specified timeout and polling interval.
     * This method is useful for cases where specific wait conditions need to be applied to ensure each element is visible at least once.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of web elements to check for visibility.
     * @return True if each element in the list becomes visible at least once within the wait time; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areMandatoryMessagesDisplayedOnce(List<WebElement> mandatoryMessages) {
     *     return elementVisibilityHandler.areAllElementsVisibleOnce(Duration.ofSeconds(10), Duration.ofMillis(500), mandatoryMessages);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisibleOnce(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeVisibleOnce(fluentWait, webElementsList);
    }

    /**
     * Waits for each of the specified web elements to become visible at least once using the default FluentWait.
     * This method is useful for ensuring that each element is visible at least once during the wait period.
     *
     * @param webElements The web elements to check.
     * @return True if each element becomes visible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areLoginElementsDisplayedOnce() {
     *     return elementVisibilityHandler.areAllElementsVisibleOnce(emailField, passwordField, loginButton);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisibleOnce(WebElement... webElements) {
        return waitForAllElements.waitForEachElementToBeVisibleOnce(defaultFluentWait, webElements);
    }

    /**
     * Waits for each of the specified web elements to become visible at least once within a specified timeout and polling interval.
     * This method is useful when you need more control over the wait time and polling frequency to ensure each element is visible at least once.
     *
     * @param timeout         The maximum time to wait for the elements to become visible.
     * @param pollingInterval The interval to poll for element visibility.
     * @param webElements     The web elements to check.
     * @return True if each element becomes visible at least once, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areImportantMessagesDisplayedOnce() {
     *     return elementVisibilityHandler.areAllElementsVisibleOnce(Duration.ofSeconds(5), Duration.ofMillis(500), importantMessage1, importantMessage2);
     * }
     * }</pre>
     */
    public boolean areAllElementsVisibleOnce(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeVisibleOnce(fluentWait, webElements);
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
}
