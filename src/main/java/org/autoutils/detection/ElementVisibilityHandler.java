package org.autoutils.detection;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

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

    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;
    private final FluentWait<WebDriver> defaultFluentWait;

    /**
     * Constructor for ElementVisibilityHandler.
     *
     * @param webDriver            The WebDriver instance to be used for visibility checks.
     * @param webDriverWait     The WebDriverWait instance to be used for waiting for elements.
     * @param defaultFluentWait The default FluentWait instance provided by the target project.
     */
    public ElementVisibilityHandler(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
        this.defaultFluentWait = defaultFluentWait;
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
     *         or is not visible within the default wait time.
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
     *         Returns false if the element is not found or is not visible within the given timeout.
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
     *         Returns false if the text does not appear within the given timeout.
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
     *         Returns false if the text does not appear within the given timeout.
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
     *         Returns false if the text does not appear in the element's value within the default wait time.
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
     *         Returns false if the text does not appear within the given timeout.
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
     * public boolean isErrorTextPatternInvisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternInvisible(By.id("errorTooltip"), Pattern.compile("Error.*"));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternInvisible(By locator, Pattern pattern) {
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
     * public boolean isErrorTextPatternInvisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternInvisible(By.id("errorTooltip"), Pattern.compile("Error.*"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternInvisible(By locator, Pattern pattern, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(webDriver, timeout);
            return customWait.until(ExpectedConditions.textMatches(locator, pattern));
        } catch (TimeoutException exception) {
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
     * public boolean isErrorTextPatternInvisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternInvisible(errorElement, Pattern.compile("Error.*"));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternInvisible(WebElement element, Pattern pattern) {
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
     * public boolean isErrorTextPatternInvisible() {
     *     return elementVisibilityHandler.isElementTextMatchingPatternInvisible(errorElement, Pattern.compile("Error.*"), Duration.ofSeconds(5));
     * }
     * }</pre>
     */
    public boolean isElementTextMatchingPatternInvisible(WebElement element, Pattern pattern, Duration timeout) {
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

}