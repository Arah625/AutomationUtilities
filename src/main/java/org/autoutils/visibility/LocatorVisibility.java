package org.autoutils.visibility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The `LocatorVisibility` class provides methods for checking the visibility and presence of web elements
 * identified by locators using various wait strategies. It is designed to ensure that specific elements
 * are present in the DOM and visible on the page, which is essential for validating UI conditions
 * in dynamic web applications.
 */
public class LocatorVisibility extends AbstractVisibilityHandler {
    private final WaitForAllLocators waitForAllLocators;
    private final WaitForFirstLocator waitForFirstLocator;

    /**
     * Constructs an instance of `LocatorVisibility`.
     *
     * @param webDriver         The WebDriver instance to be used.
     * @param webDriverWait     The WebDriverWait instance to be used.
     * @param defaultFluentWait The FluentWait instance to be used.
     */
    protected LocatorVisibility(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForAllLocators = new WaitForAllLocators(webDriver);
        this.waitForFirstLocator = new WaitForFirstLocator(webDriver);
    }

    /**
     * Checks the visibility of an element identified by a locator using the default WebDriverWait.
     * This method ensures that the element is both present in the DOM and visible on the page.
     *
     * <p>It first checks for the presence of the element in the DOM using `presenceOfElementLocated`,
     * which confirms that the element exists. Then, it checks for visibility using `visibilityOfElementLocated`,
     * ensuring that the element is not only present but also visible to the user (i.e., not hidden or off-screen).</p>
     *
     * <p>This combination is useful in scenarios where an element might exist in the DOM but not be visible
     * due to being hidden or having display properties that make it invisible.</p>
     *
     * @param locator The locator identifying the element.
     * @return True if the element is present and visible; false otherwise. Returns false if the element is not found
     * or is not visible within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isConfirmationMessageVisible() {
     *     return elementVisibilityHandler.isElementVisible(By.id("confirmationMessage"));
     * }
     * }</pre>
     */
    public boolean isElementVisible(By locator) {
        try {
            return webDriverWait.until(ExpectedConditions.and(
                    ExpectedConditions.presenceOfElementLocated(locator),
                    ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks the visibility of an element identified by a locator within a specified timeout using a custom WebDriverWait.
     * This method ensures that the element is both present in the DOM and visible on the page within the given timeout.
     *
     * <p>It first checks for the presence of the element in the DOM using `presenceOfElementLocated`,
     * which confirms that the element exists. Then, it checks for visibility using `visibilityOfElementLocated`,
     * ensuring that the element is not only present but also visible to the user (i.e., not hidden or off-screen).</p>
     *
     * <p>This combination is particularly useful when working with dynamic content where elements might
     * load in the DOM but take some time to become visible, or where an element might be conditionally displayed.</p>
     *
     * @param locator The locator identifying the element.
     * @param timeout The maximum duration to wait for the element's visibility.
     * @return True if the element is present and visible within the specified timeout; false otherwise.
     * Returns false if the element is not found or is not visible within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isLoginErrorMessageVisible() {
     *     return elementVisibilityHandler.isElementVisible(By.id("loginError"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementVisible(By locator, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.and(
                    ExpectedConditions.presenceOfElementLocated(locator),
                    ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks the presence of a specific web element in the DOM without considering visibility.
     * This method is useful when you need to verify that an element exists in the DOM, regardless of whether it is visible.
     *
     * @param locator The locator identifying the element.
     * @return True if the element is present in the DOM; false otherwise. Returns false if the element is not found within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isPasswordFieldPresent() {
     *     return elementVisibilityHandler.isElementPresent(By.id("password"));
     * }
     * }</pre>
     */
    public boolean isElementPresent(By locator) {
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks the presence of a specific web element in the DOM without considering visibility using a custom wait.
     * This method is particularly useful for elements that might take longer to load into the DOM, allowing for a custom timeout period.
     *
     * @param locator The locator identifying the element.
     * @param timeout The maximum duration to wait for the element's presence.
     * @return True if the element is present in the DOM; false otherwise. Returns false if the element is not found within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isPasswordFieldPresent() {
     *     return elementVisibilityHandler.isElementPresent(By.id("password"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementPresent(By locator, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if a specific text is present in an element identified by a locator.
     * This method is useful for scenarios where the element is identified by a specific strategy (e.g., By.id, By.xpath),
     * and you need to verify that a specific text has appeared within that element.
     *
     * @param locator The locator identifying the element.
     * @param text    The text to verify within the element identified by the locator.
     * @return True if the text is present within the element identified by the locator, false otherwise.
     * Returns false if the text does not appear within the element within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed() {
     *     return elementVisibilityHandler.isTextPresentInElement(By.id("errorTooltip"), "Error message");
     * }
     * }</pre>
     */
    public boolean isElementContainingTextVisible(By locator, String text) {
        try {
            return webDriverWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if a specific text is present in an element identified by a locator using a custom wait.
     * This method is ideal for cases where the text might take longer to appear in the element,
     * allowing you to specify a custom timeout for the wait.
     *
     * @param locator The locator identifying the element.
     * @param text    The text to verify within the element identified by the locator.
     * @param timeout The maximum duration to wait for the text to be present within the element.
     * @return True if the text is present within the element identified by the locator within the specified timeout, false otherwise.
     * Returns false if the text does not appear within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed() {
     *     return elementVisibilityHandler.isTextPresentInElement(By.id("errorTooltip"), "Error message", Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementContainingTextVisible(By locator, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if a specific text is present in the value attribute of an element identified by a locator.
     * This method is useful for verifying that a specific text is present within the value attribute of an element,
     * which is commonly used for form fields like input boxes.
     *
     * @param locator The locator identifying the element.
     * @param text    The text to verify within the value attribute of the element.
     * @return True if the text is present within the value attribute of the element, false otherwise.
     * Returns false if the text does not appear in the element's value within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isUsernameFieldPrefilled(String username) {
     *     return elementVisibilityHandler.isTextContainedInElementValueAttribute(By.id("username"), username);
     * }
     * }</pre>
     */
    public boolean isTextContainedInElementValueAttribute(By locator, String text) {
        try {
            return webDriverWait.until(ExpectedConditions.textToBePresentInElementValue(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if a specific text is present in the value attribute of an element identified by a locator using a custom wait.
     * This method is ideal for cases where the text might take longer to appear in the value attribute of the element,
     * allowing you to specify a custom timeout for the wait.
     *
     * @param locator The locator identifying the element.
     * @param text    The text to verify within the value attribute of the element.
     * @param timeout The maximum duration to wait for the text to be present within the element's value attribute.
     * @return True if the text is present within the value attribute of the element within the specified timeout, false otherwise.
     * Returns false if the text does not appear within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isUsernameFieldPrefilled(String username) {
     *     return elementVisibilityHandler.isTextContainedInElementValueAttribute(By.id("username"), username, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isTextContainedInElementValueAttribute(By locator, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.textToBePresentInElementValue(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if the text of an element identified by a locator matches a specific pattern.
     * This method is useful when you need to verify that the text within an element matches a pattern (e.g., regex).
     *
     * @param locator The locator identifying the element.
     * @param pattern The pattern to match the text of the element.
     * @return True if the text of the element matches the pattern; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTextPatternVisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternVisible(By.id("errorTooltip"), Pattern.compile("Error.*"));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternVisible(By locator, Pattern pattern) {
        try {
            return webDriverWait.until(ExpectedConditions.textMatches(locator, pattern));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks if the text of an element identified by a locator matches a specific pattern within a specified timeout.
     * This method is useful when you need to verify that the text within an element matches a pattern (e.g., regex) within a specific timeframe.
     *
     * @param locator The locator identifying the element.
     * @param pattern The pattern to match the text of the element.
     * @param timeout The maximum duration to wait for the text to match the pattern.
     * @return True if the text of the element matches the pattern within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTextPatternVisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternVisible(By.id("errorTooltip"), Pattern.compile("Error.*"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternVisible(By locator, Pattern pattern, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.textMatches(locator, pattern));
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks if the exact text is present in an element identified by a locator.
     * This method is useful when you need to verify that an element contains the exact specified text.
     *
     * @param locator The locator identifying the element.
     * @param text    The exact text to verify within the element.
     * @return True if the element contains the exact text; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isExactErrorTextVisible() {
     *     return elementVisibilityHandler.isExactTextPresentInElement(By.id("errorTooltip"), "Specific Error Message");
     * }
     * }</pre>
     */
    public boolean isExactTextPresentInElement(By locator, String text) {
        try {
            return webDriverWait.until(ExpectedConditions.textToBe(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if the exact text is present in an element identified by a locator within a specified timeout.
     * This method is useful when you need to verify that an element contains the exact specified text within a specific timeframe.
     *
     * @param locator The locator identifying the element.
     * @param text    The exact text to verify within the element.
     * @param timeout The maximum duration to wait for the exact text to be present within the element.
     * @return True if the element contains the exact text within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isExactErrorTextVisible() {
     *     return elementVisibilityHandler.isExactTextPresentInElement(By.id("errorTooltip"), "Specific Error Message", Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isExactTextPresentInElement(By locator, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.textToBe(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Verifies if a specific number of elements identified by locators are visible.
     * This method is particularly useful for scenarios where elements are identified by locators, and you need to verify
     * that a certain number of elements located by these locators are visible.
     *
     * @param locatorsList             A list of locators identifying elements.
     * @param expectedNumberOfElements The expected number of visible elements.
     * @return True if the number of visible elements matches the expected number; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areVisibleHintsEqualTo(int expectedHintCount) {
     *     return elementVisibilityHandler.isNumberOfElementsByLocatorsVisible(passwordHintLocators, expectedHintCount);
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByLocatorsVisible(List<By> locatorsList, int expectedNumberOfElements) {
        try {
            int visibleElementsCount = 0;
            for (By locator : locatorsList) {
                List<WebElement> elements = webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
                visibleElementsCount += elements.size();
            }
            return visibleElementsCount == expectedNumberOfElements;
        } catch (NoSuchElementException | TimeoutException exception) {
            return false;
        }
    }

    /**
     * Verifies if a specific number of elements identified by locators are visible within a specified timeout using a custom WebDriverWait.
     * This method allows for a custom timeout, ensuring that the required number of elements become visible within a specified timeframe.
     *
     * @param locatorsList             A list of locators identifying elements.
     * @param expectedNumberOfElements The expected number of visible elements.
     * @param timeout                  The maximum duration to wait for the elements' visibility.
     * @return True if the number of visible elements matches the expected number within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areVisibleWarningsEqualTo(int expectedCount) {
     *     return elementVisibilityHandler.isNumberOfElementsByLocatorsVisible(warningLocatorsList, expectedCount, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByLocatorsVisible(List<By> locatorsList, int expectedNumberOfElements, Duration timeout) {
        try {
            int visibleElementsCount = 0;
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            for (By locator : locatorsList) {
                List<WebElement> elements = customWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
                visibleElementsCount += elements.size();
            }
            return visibleElementsCount == expectedNumberOfElements;
        } catch (NoSuchElementException | TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks if all elements identified by a list of locators are visible.
     * This method simplifies the verification process by assuming that the expected number of visible elements
     * is equal to the total number of elements identified by the locators in the provided list.
     *
     * @param locatorsList A list of locators identifying elements.
     * @return True if all elements identified by the locators are visible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllTooltipWarningsVisible() {
     *     return elementVisibilityHandler.isNumberOfElementsByLocatorsVisible(tooltipWarningLocators);
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByLocatorsVisible(List<By> locatorsList) {
        return isNumberOfElementsByLocatorsVisible(locatorsList, locatorsList.size());
    }

    /**
     * Checks if all elements identified by a list of locators are visible within a specified timeout using a custom WebDriverWait.
     * This method is useful when you need to ensure that all elements located by the given locators become visible within a specific timeframe.
     *
     * @param locatorsList A list of locators identifying elements.
     * @param timeout      The maximum duration to wait for the elements' visibility.
     * @return True if all elements identified by the locators are visible within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllFormHintsVisible() {
     *     return elementVisibilityHandler.isNumberOfElementsByLocatorsVisible(formHintLocators, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByLocatorsVisible(List<By> locatorsList, Duration timeout) {
        return isNumberOfElementsByLocatorsVisible(locatorsList, locatorsList.size(), timeout);
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
}
