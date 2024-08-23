package org.autoutils.detection;

import org.autoutils.detection.wait.element.WaitForAllElements;
import org.autoutils.detection.wait.element.WaitForFirstElement;
import org.autoutils.detection.wait.locator.WaitForAllLocators;
import org.autoutils.detection.wait.locator.WaitForFirstLocator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Handles the visibility checks of web elements and locators within a web page. It provides
 * methods to wait for the visibility of elements or locators, either individually or in groups,
 * before proceeding with further actions or assertions in automated tests.
 *
 * <p>Example usage in a screen class:</p>
 * <pre>{@code
 * public class LoginScreen {
 *     private WebDriver driver;
 *     private WebDriverWait webDriverWait;
 *     private ElementVisibilityHandler elementVisibilityHandler;
 *
 *     @FindBy(id = "email")
 *     private WebElement emailField;
 *
 *     @FindBy(id = "password")
 *     private WebElement passwordField;
 *
 *     @FindBy(id = "loginButton")
 *     private WebElement loginButton;
 *
 *     @FindBy(id = "emailAlert")
 *     private WebElement emailAlert;
 *
 *     public LoginScreen(WebDriver driver) {
 *         this.driver = driver;
 *         this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
 *         FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
 *                 .withTimeout(Duration.ofSeconds(30))
 *                 .pollingEvery(Duration.ofMillis(500))
 *                 .ignoring(NoSuchElementException.class);
 *         this.elementVisibilityHandler = new ElementVisibilityHandler(driver, webDriverWait, fluentWait);
 *         PageFactory.initElements(driver, this);
 *     }
 *
 *     public boolean isEmailAlertVisible() {
 *         return elementVisibilityHandler.isElementVisible(emailAlert);
 *     }
 *
 *     public boolean areLoginFieldsVisible() {
 *         return elementVisibilityHandler.areAllElementsVisible(emailField, passwordField, loginButton);
 *     }
 * }
 * }</pre>
 */
public class ElementVisibilityHandler {

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final FluentWait<WebDriver> defaultFluentWait;
    protected final WaitForAllElements waitForAllElements;
    protected final WaitForFirstElement waitForFirstElement;
    protected final WaitForAllLocators waitForAllLocators;
    protected final WaitForFirstLocator waitForFirstLocator;

    /**
     * Constructor for ElementVisibilityHandler.
     *
     * @param driver            The WebDriver instance to be used for visibility checks.
     * @param webDriverWait     The WebDriverWait instance to be used for waiting for elements.
     * @param defaultFluentWait The default FluentWait instance provided by the target project.
     */
    public ElementVisibilityHandler(WebDriver driver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.defaultFluentWait = defaultFluentWait;
        this.waitForAllElements = new WaitForAllElements(driver);
        this.waitForFirstElement = new WaitForFirstElement(driver);
        this.waitForAllLocators = new WaitForAllLocators(driver);
        this.waitForFirstLocator = new WaitForFirstLocator(driver);
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

    public boolean areAllElementsVisibleOnce(List<WebElement> webElementsList) {
        return waitForAllElements.waitForEachElementToBeVisibleOnce(defaultFluentWait, webElementsList);
    }

    public boolean areAllElementsVisibleOnce(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeVisibleOnce(fluentWait, webElementsList);
    }

    public boolean areAllElementsVisibleOnce(WebElement... webElements) {
        return waitForAllElements.waitForEachElementToBeVisibleOnce(defaultFluentWait, webElements);
    }

    public boolean areAllElementsVisibleOnce(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeVisibleOnce(fluentWait, webElements);
    }

    public boolean areAllElementsByLocatorsVisible(List<By> locatorsList) {
        return waitForAllLocators.waitForVisibilityOfAllLocators(defaultFluentWait, locatorsList);
    }

    public boolean areAllElementsByLocatorsVisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForVisibilityOfAllLocators(fluentWait, locatorsList);
    }

    public boolean areAllElementsByLocatorsVisible(By... locators) {
        return waitForAllLocators.waitForVisibilityOfAllLocators(defaultFluentWait, locators);
    }

    public boolean areAllElementsByLocatorsVisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForVisibilityOfAllLocators(fluentWait, locators);
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
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks the visibility of an element identified by a locator using the default WebDriverWait.
     * This method is useful for confirming that an element located by a specific strategy (e.g., By.id, By.xpath) is visible.
     *
     * @param locator The locator identifying the element.
     * @return True if the element is visible; false otherwise. Returns false if the element is not found or if it is not visible within the default wait time.
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
        } // TODO: 23.08.2024 more descriptive javadoc, dlaczego presence i visibility
    }

    /**
     * Checks the visibility of an element identified by a locator within a specified timeout using a custom WebDriverWait.
     * This method is ideal for scenarios where the visibility of an element located by a specific strategy needs to be confirmed within a custom time frame.
     *
     * @param locator The locator identifying the element.
     * @param timeout The maximum duration to wait for the element's visibility.
     * @return True if the element is visible within the specified timeout; false otherwise. Returns false if the element is not found or is not visible within the given timeout.
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
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.and(
                    ExpectedConditions.presenceOfElementLocated(locator),
                    ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        } // TODO: 23.08.2024 more descriptive javadoc, dlaczego presence i visibility
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
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
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
     *         Returns false if the text does not appear in the element within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed(String errorMessage) {
     *     return elementVisibilityHandler.isTextPresentInElement(errorElement, errorMessage);
     * }
     * }</pre>
     */
    public boolean isTextPresentInElement(WebElement webElement, String text) {
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
     *         Returns false if the text does not appear within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed(String errorMessage) {
     *     return elementVisibilityHandler.isTextPresentInElement(errorElement, errorMessage, Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isTextPresentInElement(WebElement webElement, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.textToBePresentInElement(webElement, text));
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
     *         Returns false if the text does not appear within the element within the default wait time.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed() {
     *     return elementVisibilityHandler.isTextPresentInElement(By.id("errorTooltip"), "Error message");
     * }
     * }</pre>
     */
    public boolean isTextPresentInElement(By locator, String text) {
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
     *         Returns false if the text does not appear within the given timeout.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean isErrorMessageDisplayed() {
     *     return elementVisibilityHandler.isTextPresentInElement(By.id("errorTooltip"), "Error message", Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isTextPresentInElement(By locator, String text, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
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
    public boolean invisibilityOfElement(WebElement webElement) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOf(webElement));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    } // TODO: 23.08.2024 czy na pewno NoSuchElementException? tutaj c hodzi o to, zeby byl niewidoczny?

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
    public boolean invisibilityOfElement(WebElement webElement, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.invisibilityOf(webElement));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
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
    public boolean invisibilityOfElement(By locator) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException | NoSuchElementException exception) {
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
    public boolean invisibilityOfElement(By locator, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
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

    public boolean areAllElementsByLocatorsInvisible(List<By> locatorsList) {
        return waitForAllLocators.waitForInvisibilityOfAllLocators(defaultFluentWait, locatorsList);
    }

    public boolean areAllElementsByLocatorsInvisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForInvisibilityOfAllLocators(fluentWait, locatorsList);
    }

    public boolean areAllElementsByLocatorsInvisible(By... locators) {
        return waitForAllLocators.waitForInvisibilityOfAllLocators(defaultFluentWait, locators);
    }

    public boolean areAllElementsByLocatorsInvisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForInvisibilityOfAllLocators(fluentWait, locators);
    }

    public boolean areAllElementsByLocatorsInvisibleOnce(By... locators) {
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(defaultFluentWait, locators);
    }

    public boolean areAllElementsByLocatorsInvisibleOnce(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(fluentWait, locators);
    }

    public boolean areAllElementsByLocatorsInvisibleOnce(List<By> locatorsList) {
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(defaultFluentWait, locatorsList);
    }

    public boolean areAllElementsByLocatorsInvisibleOnce(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllLocators.waitForEachLocatorToBeInvisibleOnce(fluentWait, locatorsList);
    }

    public boolean isAnyElementInvisible(List<WebElement> webElementsList) {
        return waitForFirstElement.waitForInvisibilityOfFirstElement(defaultFluentWait, webElementsList);
    }

    public boolean isAnyElementInvisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstElement.waitForInvisibilityOfFirstElement(fluentWait, webElementsList);
    }

    public boolean isAnyElementInvisible(WebElement... webElement) {
        return waitForFirstElement.waitForInvisibilityOfFirstElement(defaultFluentWait, webElement);
    }

    public boolean isAnyElementInvisible(Duration timeout, Duration pollingInterval, WebElement... webElement) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForFirstElement.waitForInvisibilityOfFirstElement(fluentWait, webElement);
    }

    public boolean areAllElementsInvisible(List<WebElement> webElementList) {
        return waitForAllElements.waitForInvisibilityOfAllElements(defaultFluentWait, webElementList);
    }

    public boolean areAllElementsInvisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForInvisibilityOfAllElements(fluentWait, webElementList);
    }

    public boolean areAllElementsInvisible(WebElement... webElements) {
        return waitForAllElements.waitForInvisibilityOfAllElements(defaultFluentWait, webElements);
    }

    public boolean areAllElementsInvisible(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForInvisibilityOfAllElements(fluentWait, webElements);
    }

    public boolean areAllElementsByLocatorsInvisibleOnce(List<WebElement> webElementList) {
        return waitForAllElements.waitForEachElementToBeInvisibleOnce(defaultFluentWait, webElementList);
    }

    public boolean areAllElementsByLocatorsInvisibleOnce(Duration timeout, Duration pollingInterval, List<WebElement> webElementList) {
        FluentWait<WebDriver> fluentWait = createCustomFluentWait(timeout, pollingInterval);
        return waitForAllElements.waitForEachElementToBeInvisibleOnce(fluentWait, webElementList);
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
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
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
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
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
    private FluentWait<WebDriver> createCustomFluentWait(Duration timeout, Duration pollingInterval) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
    }
}
// TODO: 23.08.2024 Dodac metody na invisible
// TODO: 23.08.2024 zrobic commit, pozniej zmienic dostepy na packageprivate zrobnic z obecnej klasy fasade, dodac module-info zamiast package info
// TODO: 23.08.2024 zastanowic sie nad rozbiciem tej metody na dwie - visbility i invisibility handlery