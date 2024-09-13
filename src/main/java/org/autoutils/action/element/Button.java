package org.autoutils.action.element;

import org.autoutils.detection.ElementFinder;
import org.autoutils.retry.ActionHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The {@code Button} class provides methods for interacting with button elements
 * across different platforms (web, iOS, Android). It allows users to check the state of a button
 * (enabled/disabled) by evaluating platform-specific attributes or class names.
 * This class implements {@link ElementState}, providing a unified interface for state checks.
 *
 * <p>
 * It abstracts the logic for identifying if a button is enabled by checking attributes or classes,
 * making it platform-agnostic.
 * </p>
 *
 * <p>
 * Example Initialization:
 * <pre>{@code
 * // Initialize the Button class
 * Button buttonHelper = new Button();
 * }</pre>
 * <p>
 * Example usage in a custom utility class:
 * <pre>{@code
 * public class CustomButtonUtils {
 *     private final Button buttonHelper;
 *
 *     public CustomButtonUtils() {
 *         this.buttonHelper = new Button();
 *     }
 *
 *     public boolean isCustomButtonEnabled(By buttonLocator) {
 *         return buttonHelper.isButtonEnabled(buttonLocator, "disabled", "false");
 *     }
 * }
 * }</pre>
 * <p>
 * Example usage in a Page Object method with {@code @FindBy}:
 * <pre>{@code
 * public class SettingsPage {
 *
 *     @FindBy(id = "saveButton")
 *     private WebElement saveButton;
 *
 *     private final Button buttonHelper = new Button();
 *
 *     public boolean isSaveButtonEnabled() {
 *         return buttonHelper.isButtonEnabled(saveButton, "disabled", "false");
 *     }
 * }
 * }</pre>
 */
public class Button implements ElementState {

    private final WebDriverWait webDriverWait;
    private final ElementFinder elementFinder;

    /**
     * Constructs a {@code Button} instance using a provided {@code WebDriver} and {@code WebDriverWait}.
     *
     * @param webDriver the WebDriver instance to be used for finding elements
     * @param webDriverWait the WebDriverWait instance to be used for waiting for elements
     */
    public Button(WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriverWait = webDriverWait;
        this.elementFinder = new ElementFinder(webDriver, webDriverWait);
    }

    /**
     * Checks if a button is enabled based on a specified attribute and disable value using a {@link WebElement}.
     * This method abstracts platform-specific checks like "disabled" or "enabled".
     *
     * @param webElement   the WebElement representing the button
     * @param attribute    the attribute used to check the state (e.g., "disabled", "enabled")
     * @param disableValue the value indicating the button is disabled (e.g., "true", "1")
     * @return true if the button is enabled, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isCustomButtonEnabled(WebElement buttonElement) {
     *     return buttonHelper.isButtonEnabled(buttonElement, "disabled", "false");
     * }
     * }</pre>
     * </p>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * @FindBy(id = "submitButton")
     * private WebElement submitButton;
     *
     * public boolean isSubmitButtonEnabled() {
     *     return buttonHelper.isButtonEnabled(submitButton, "disabled", "false");
     * }
     * }</pre>
     */
    public boolean isButtonEnabled(WebElement webElement, String attribute, String disableValue) {
        return isButtonEnabled(webElement, attribute, disableValue, 3);  // Default retry to 3
    }

    /**
     * Checks if a button is enabled based on a specified attribute and disable value using a {@link By} locator.
     * This method abstracts platform-specific checks like "disabled" or "enabled".
     *
     * @param locator      the {@code By} locator for the button element
     * @param attribute    the attribute used to check the state (e.g., "disabled", "enabled")
     * @param disableValue the value indicating the button is disabled (e.g., "true", "1")
     * @return true if the button is enabled, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isCustomButtonEnabled(By buttonLocator) {
     *     return buttonHelper.isButtonEnabled(buttonLocator, "disabled", "false");
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * public boolean isDeleteButtonEnabled(By buttonLocator) {
     *     return buttonHelper.isButtonEnabled(buttonLocator, "disabled", "false");
     * }
     * }</pre>
     */
    public boolean isButtonEnabled(By locator, String attribute, String disableValue) {
        return isButtonEnabled(locator, attribute, disableValue, 3);  // Default retry to 3
    }

    /**
     * Checks if a button is enabled based on a specified attribute and disable value using a {@link WebElement}, with retries.
     * This method abstracts platform-specific checks like "disabled" or "enabled".
     *
     * @param webElement   the WebElement representing the button
     * @param attribute    the attribute used to check the state (e.g., "disabled", "enabled")
     * @param disableValue the value indicating the button is disabled (e.g., "true", "1")
     * @param retries      the number of times to retry the action if it fails
     * @return true if the button is enabled, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isCustomButtonEnabledWithRetry(WebElement buttonElement) {
     *     return buttonHelper.isButtonEnabled(buttonElement, "disabled", "false", 5);
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * @FindBy(id = "submitButton")
     * private WebElement submitButton;
     *
     * public boolean isSubmitButtonEnabledWithRetry() {
     *     return buttonHelper.isButtonEnabled(submitButton, "disabled", "false", 5);
     * }
     * }</pre>
     */
    public boolean isButtonEnabled(WebElement webElement, String attribute, String disableValue, int retries) {
        try {
            String state = ActionHandler.retryStringAction(() -> getElementStateByAttribute(webElement, attribute), retries);
            return state == null || !state.equalsIgnoreCase(disableValue);
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if a button is enabled based on a specified attribute and disable value using a {@link By} locator, with retries.
     * This method abstracts platform-specific checks like "disabled" or "enabled".
     *
     * @param locator      the {@code By} locator for the button element
     * @param attribute    the attribute used to check the state (e.g., "disabled", "enabled")
     * @param disableValue the value indicating the button is disabled (e.g., "true", "1")
     * @param retries      the number of times to retry the action if it fails
     * @return true if the button is enabled, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isCustomButtonEnabledWithRetry(By buttonLocator) {
     *     return buttonHelper.isButtonEnabled(buttonLocator, "disabled", "false", 4);
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * public boolean isSaveButtonEnabledWithRetry(By buttonLocator) {
     *     return buttonHelper.isButtonEnabled(buttonLocator, "disabled", "false", 4);
     * }
     * }</pre>
     */
    public boolean isButtonEnabled(By locator, String attribute, String disableValue, int retries) {
        try {
            String state = ActionHandler.retryStringAction(() -> getElementStateByAttribute(locator, attribute), retries);
            return state == null || !state.equalsIgnoreCase(disableValue);
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Retrieves the value of a specific attribute for a {@link WebElement}.
     * This method is used to determine the state of the element based on the attribute's value.
     *
     * @param webElement the WebElement whose state is being retrieved
     * @param attribute  the attribute to evaluate (e.g., "disabled", "enabled")
     * @return a string representing the state of the element based on the attribute value, or null if the attribute is not present
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public String getCustomElementState(WebElement element) {
     *     return buttonHelper.getElementStateByAttribute(element, "disabled");
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * @FindBy(id = "submitButton")
     * private WebElement submitButton;
     *
     * public String getSubmitButtonState() {
     *     return buttonHelper.getElementStateByAttribute(submitButton, "disabled");
     * }
     * }</pre>
     */
    @Override
    public String getElementStateByAttribute(WebElement webElement, String attribute) {
        return ActionHandler.retryStringAction(() -> webElement.getAttribute(attribute), 3);
    }

    /**
     * Retrieves the value of a specific attribute for an element identified by a {@link By} locator.
     * This method is used to determine the state of the element based on the attribute's value.
     *
     * @param locator   the {@code By} locator used to find the element
     * @param attribute the attribute to evaluate (e.g., "disabled", "enabled")
     * @return a string representing the state of the element based on the attribute value, or null if the attribute is not present
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public String getCustomElementState(By locator) {
     *     return buttonHelper.getElementStateByAttribute(locator, "disabled");
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * public String getResetButtonState(By buttonLocator) {
     *     return buttonHelper.getElementStateByAttribute(buttonLocator, "disabled");
     * }
     * }</pre>
     */
    @Override
    public String getElementStateByAttribute(By locator, String attribute) {
        return ActionHandler.retryStringAction(() -> {
            WebElement webElement = elementFinder.findElement(locator);
            webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
            return webElement.getAttribute(attribute);
        }, 3);
    }

    /**
     * Checks whether a specific class is present in the {@code class} attribute of a {@link WebElement}.
     * <p>
     * This method retrieves the {@code class} attribute from the provided WebElement and checks whether
     * the specified {@code className} is present as a substring within the attribute. If the {@code class}
     * attribute is not present (i.e., null) or if it does not contain the specified {@code className},
     * the method returns {@code false}.
     *
     * @param webElement the WebElement whose class attribute is being checked
     * @param className  the class name to check for
     * @return true if the class is present, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isClassPresentOnCustomButton(WebElement buttonElement) {
     *     return buttonHelper.isClassPresent(buttonElement, "active");
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * @FindBy(id = "submitButton")
     * private WebElement submitButton;
     *
     * public boolean isButtonActive() {
     *     return buttonHelper.isClassPresent(submitButton, "active");
     * }
     * }</pre>
     */
    public boolean isClassPresent(WebElement webElement, String className) {
        return isClassPresent(webElement, className, 3);  // Default retry to 3
    }

    /**
     * Checks whether a specific class is present in the {@code class} attribute of an element identified by a {@link By} locator.
     * <p>
     * This method retrieves the {@code class} attribute from the element located by the given {@code By} locator
     * and checks whether the specified {@code className} is present as a substring within the attribute. If the {@code class}
     * attribute is not present (i.e., null) or if it does not contain the specified {@code className},
     * the method returns {@code false}.
     *
     * @param locator   the {@code By} locator used to find the element
     * @param className the class name to check for
     * @return true if the class is present, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isClassPresentOnCustomButton(By locator) {
     *     return buttonHelper.isClassPresent(locator, "active");
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * public boolean isButtonActive(By buttonLocator) {
     *     return buttonHelper.isClassPresent(buttonLocator, "active");
     * }
     * }</pre>
     */
    public boolean isClassPresent(By locator, String className) {
        return isClassPresent(locator, className, 3);  // Default retry to 3
    }
    
    /**
     * Checks whether a specific class is present in the {@code class} attribute of a {@link WebElement}, with retries.
     * <p>
     * This method retrieves the {@code class} attribute from the provided WebElement and checks whether
     * the specified {@code className} is present as a substring within the attribute. If the {@code class}
     * attribute is not present (i.e., null) or if it does not contain the specified {@code className},
     * the method returns {@code false}.
     *
     * @param webElement the WebElement whose class attribute is being checked
     * @param className  the class name to check for
     * @param retries    the number of times to retry the action if it fails
     * @return true if the class is present, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isClassPresentOnCustomButtonWithRetry(WebElement buttonElement) {
     *     return buttonHelper.isClassPresent(buttonElement, "active", 4);
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * @FindBy(id = "submitButton")
     * private WebElement submitButton;
     *
     * public boolean isButtonActiveWithRetry() {
     *     return buttonHelper.isClassPresent(submitButton, "active", 4);
     * }
     * }</pre>
     */
    public boolean isClassPresent(WebElement webElement, String className, int retries) {
        String classAttribute = ActionHandler.retryStringAction(() -> getElementStateByAttribute(webElement, "class"), retries);
        return classAttribute != null && classAttribute.contains(className);
    }

    /**
     * Checks whether a specific class is present in the {@code class} attribute of an element identified by a {@link By} locator, with retries.
     * <p>
     * This method retrieves the {@code class} attribute from the element located by the given {@code By} locator
     * and checks whether the specified {@code className} is present as a substring within the attribute. If the {@code class}
     * attribute is not present (i.e., null) or if it does not contain the specified {@code className},
     * the method returns {@code false}.
     *
     * @param locator   the {@code By} locator used to find the element
     * @param className the class name to check for
     * @param retries   the number of times to retry the action if it fails
     * @return true if the class is present, false otherwise
     *
     * <p>
     * Example usage in a custom utility class:
     * <pre>{@code
     * public boolean isClassPresentOnCustomButtonWithRetry(By locator) {
     *     return buttonHelper.isClassPresent(locator, "active", 3);
     * }
     * }</pre>
     * <p>
     * Example usage in a Page Object method:
     * <pre>{@code
     * public boolean isButtonActiveWithRetry(By buttonLocator) {
     *     return buttonHelper.isClassPresent(buttonLocator, "active", 3);
     * }
     * }</pre>
     */
    public boolean isClassPresent(By locator, String className, int retries) {
        String classAttribute = ActionHandler.retryStringAction(() -> getElementStateByAttribute(locator, "class"), retries);
        return classAttribute != null && classAttribute.contains(className);
    }
}
