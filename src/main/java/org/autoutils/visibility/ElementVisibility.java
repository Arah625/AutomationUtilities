package org.autoutils.visibility;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The `ElementVisibility` class provides methods for checking the visibility of web elements
 * using various wait strategies. It ensures that elements are visible before interacting with them,
 * making it essential for automated UI testing in dynamic web applications.
 */
public class ElementVisibility extends AbstractVisibilityHandler {

    private final WaitForAllElements waitForAllElements;
    private final WaitForFirstElement waitForFirstElement;

    /**
     * Constructs an instance of `ElementVisibility`.
     *
     * @param webDriver         The WebDriver instance to be used.
     * @param webDriverWait     The WebDriverWait instance to be used.
     * @param defaultFluentWait The FluentWait instance to be used.
     */
    protected ElementVisibility(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        super(webDriver, webDriverWait, defaultFluentWait);
        this.waitForAllElements = new WaitForAllElements(webDriver);
        this.waitForFirstElement = new WaitForFirstElement(webDriver);
    }

    /**
     * Checks the visibility of a specific web element using the default WebDriverWait.
     * This method is particularly useful for ensuring that a given element is visible before interacting with it.
     *
     * @param webElement The WebElement to check for visibility.
     * @return True if the element is visible; false otherwise. Returns false if the element is not found or if it is not visible within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isUniqueEmailRequiredAlertVisible() {
     *     return elementVisibilityHandler.isElementVisible(uniqueEmailRequiredAlert);
     * }
     * }</pre>
     */
    public boolean isElementVisible(WebElement webElement) {
        try {
            return webDriverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks the visibility of a specific web element within a specified timeout using a custom WebDriverWait.
     * This method allows you to specify a custom timeout duration, making it suitable for elements that might take longer to appear.
     *
     * @param webElement The WebElement to check for visibility.
     * @param timeout    The maximum duration to wait for the element's visibility.
     * @return True if the element is visible within the specified timeout; false otherwise. Returns false if the element is not found or is not visible within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isSubmitButtonVisible() {
     *     return elementVisibilityHandler.isElementVisible(submitButton, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementVisible(WebElement webElement, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if a specific text is present in a web element.
     * This method is particularly useful for verifying that a specific message, label, or other text content
     * has appeared within a web element, such as an error message or a success notification.
     *
     * @param webElement The WebElement to check.
     * @param text       The text to verify within the element.
     * @return True if the text is present within the element, false otherwise.
     * Returns false if the text does not appear in the element within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed(String errorMessage) {
     *     return elementVisibilityHandler.isTextPresentInElement(errorElement, errorMessage);
     * }
     * }</pre>
     */
    public boolean isElementContainingTextVisible(WebElement webElement, String text) {
        try {
            return webDriverWait.until(ExpectedConditions.textToBePresentInElement(webElement, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if a specific text is present in a web element using a custom wait.
     * This method allows you to specify a custom timeout, making it ideal for situations where
     * the appearance of the text may take longer than usual.
     *
     * @param webElement The WebElement to check.
     * @param text       The text to verify within the element.
     * @param timeout    The maximum duration to wait for the text to be present within the element.
     * @return True if the text is present within the element within the specified timeout, false otherwise.
     * Returns false if the text does not appear within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed(String errorMessage) {
     *     return elementVisibilityHandler.isTextPresentInElement(errorElement, errorMessage, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementContainingTextVisible(WebElement webElement, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.textToBePresentInElement(webElement, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks if the text of a specific WebElement matches a specific pattern.
     * This method is useful when you need to verify that the text within a WebElement matches a pattern (e.g., regex).
     *
     * @param element The WebElement whose text is to be matched against the pattern.
     * @param pattern The pattern to match the text of the WebElement.
     * @return True if the text of the WebElement matches the pattern; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTextPatternVisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternVisible(errorElement, Pattern.compile("Error.*"));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternVisible(WebElement element, Pattern pattern) {
        try {
            return webDriverWait.until(driver -> {
                String currentText = element.getText();
                return pattern.matcher(currentText).find();
            });
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks if the text of a specific WebElement matches a specific pattern within a specified timeout.
     * This method is useful when you need to verify that the text within a WebElement matches a pattern (e.g., regex) within a specific timeframe.
     *
     * @param element The WebElement whose text is to be matched against the pattern.
     * @param pattern The pattern to match the text of the WebElement.
     * @param timeout The maximum duration to wait for the text to match the pattern.
     * @return True if the text of the WebElement matches the pattern within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorTextPatternVisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternVisible(errorElement, Pattern.compile("Error.*"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternVisible(WebElement element, Pattern pattern, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(driver -> {
                String currentText = element.getText();
                return pattern.matcher(currentText).find();
            });
        } catch (TimeoutException exception) {
            return false;
        }
    }

    /**
     * Verifies if a specific number of WebElements in a list are visible using the default WebDriverWait.
     * This method is useful when you expect a certain number of elements to be visible on the page, and you want to validate
     * that exact count. It checks all elements in the list and returns true if the number of visible elements matches the expected count.
     *
     * @param webElementsList  A list of WebElements to check for visibility.
     * @param numberOfElements The number of elements expected to be visible.
     * @return True if the number of visible elements matches the expected number; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllStepsVisible() {
     *     return elementVisibilityHandler.isNumberOfElementsByWebElementsVisible(stepsList, 5);
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByWebElementsVisible(List<WebElement> webElementsList, int numberOfElements) {
        try {
            List<WebElement> visibleElements = webDriverWait.until(ExpectedConditions.visibilityOfAllElements(webElementsList));
            return visibleElements.size() == numberOfElements;
        } catch (NoSuchElementException | TimeoutException exception) {
            return false;
        }
    }

    /**
     * Verifies if a specific number of WebElements in a list are visible within a specified timeout using a custom WebDriverWait.
     * This method allows you to define a custom timeout for cases where visibility may take longer than usual.
     * It checks all elements in the list and returns true if the number of visible elements matches the expected count within the specified time.
     *
     * @param webElementsList  A list of WebElements to check for visibility.
     * @param numberOfElements The number of elements expected to be visible.
     * @param timeout          The maximum duration to wait for the elements' visibility.
     * @return True if the number of visible elements matches the expected number within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areRequiredFieldsVisible(int requiredCount) {
     *     return elementVisibilityHandler.isNumberOfElementsByWebElementsVisible(requiredFieldsList, requiredCount, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByWebElementsVisible(List<WebElement> webElementsList, int numberOfElements, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            List<WebElement> visibleElements = customWait.until(ExpectedConditions.visibilityOfAllElements(webElementsList));
            return visibleElements.size() == numberOfElements;
        } catch (NoSuchElementException | TimeoutException exception) {
            return false;
        }
    }

    /**
     * Checks if all WebElements in a list are visible.
     * This method simplifies the process of verifying that all elements in the provided list are visible by assuming
     * that the expected number of visible elements is equal to the total number of elements in the list.
     *
     * @param webElementsList A list of WebElements to check for visibility.
     * @return True if all elements in the list are visible; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllInstructionsVisible() {
     *     return elementVisibilityHandler.isNumberOfElementsByWebElementsVisible(instructionsList);
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByWebElementsVisible(List<WebElement> webElementsList) {
        return isNumberOfElementsByWebElementsVisible(webElementsList, webElementsList.size());
    }

    /**
     * Checks if all WebElements in a list are visible within a specified timeout using a custom WebDriverWait.
     * This method is useful when you need to ensure that all elements in the list become visible within a specific timeframe.
     *
     * @param webElementsList A list of WebElements to check for visibility.
     * @param timeout         The maximum duration to wait for the elements' visibility.
     * @return True if all elements in the list are visible within the specified timeout; false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllFormFieldsVisible() {
     *     return elementVisibilityHandler.isNumberOfElementsByWebElementsVisible(formFieldsList, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isNumberOfElementsByWebElementsVisible(List<WebElement> webElementsList, Duration timeout) {
        return isNumberOfElementsByWebElementsVisible(webElementsList, webElementsList.size(), timeout);
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
}
