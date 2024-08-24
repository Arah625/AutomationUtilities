package org.autoutils.retry;

import org.autoutils.retry.exceptions.ExceptionNotHandledException;
import org.autoutils.retry.exceptions.MaximumRetriesExceededException;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * A utility class that provides a set of methods for executing actions with retry logic.
 * It is designed to handle transient web automation exceptions that can occur due to dynamic content loading,
 * asynchronous page updates, and other common issues encountered in web automation with Selenium.
 *
 * <p>The retry logic enhances the robustness and reliability of web automation scripts by attempting
 * to perform actions multiple times (as specified) in case of specific exceptions before failing the test.</p>
 */
public class ActionHandler {

    private static final int DEFAULT_RETRY_COUNT = 1;

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * <p>The {@code ActionHandler} class is designed to provide static methods for executing actions
     * with retry logic. As a utility class, it is not meant to be instantiated.</p>
     *
     * <p>This constructor is private to enforce the non-instantiability of the class,
     * ensuring that it can only be used statically.</p>
     */
    private ActionHandler() {
        // Prevent instantiation
    }

    /**
     * Retries a supplier action up to a specified number of times if it fails due to certain exceptions.
     *
     * <p>This method is particularly useful for handling operations that may intermittently fail in web automation scenarios.</p>
     *
     * <pre>{@code
     * @FindBy(id = "retryable-action-element")
     * WebElement retryableActionElement;
     *
     * public T performActionWithRetry() {
     *     return ActionHandler.retryAction(() -> retryableActionElement.getText(), 3, NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The supplier action to be executed, which returns a value of type T.
     * @param retryCount         The number of retry attempts before giving up.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @param <T>                The type of the result returned by the action.
     * @return The result of the action if it succeeds within the retry attempts.
     * @throws ExceptionNotHandledException    If an exception is thrown that is not specified in exceptionsToHandle.
     * @throws MaximumRetriesExceededException If the number of retry attempts is exceeded without successful execution.
     */
    @SafeVarargs
    public static <T> T retryAction(Supplier<T> action, int retryCount, Class<? extends Exception>... exceptionsToHandle) {
        int attempt = 0;
        while (true) {
            try {
                return action.get();
            } catch (Exception e) {
                if (!isExceptionHandled(e, exceptionsToHandle)) {
                    throw new ExceptionNotHandledException("Unhandled exception occurred", e);
                }
                if (++attempt > retryCount) {
                    throw new MaximumRetriesExceededException("Exceeded max retry attempts", e);
                }
            }
        }
    }

    /**
     * Retries a void action up to the default number of times if it fails due to specified exceptions.
     *
     * <p>This method is particularly useful for actions that do not return a value but may fail transiently,
     * such as clicking on elements that might not be immediately clickable due to asynchronous page updates.</p>
     *
     * <pre>{@code
     * @FindBy(id = "submit-button")
     * WebElement submitButton;
     *
     * public void clickSubmitButtonWithRetry() {
     *     ActionHandler.retryVoidAction(() -> submitButton.click(), ElementClickInterceptedException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The void action to execute, encapsulated in a {@link Runnable}.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action. Only the exceptions
     *                           specified here will be handled; others will result in an immediate failure.
     */
    @SafeVarargs
    public static void retryVoidAction(Runnable action, Class<? extends Exception>... exceptionsToHandle) {
        retryAction(() -> {
            action.run();
            return null; // For compatibility with Supplier<T>
        }, DEFAULT_RETRY_COUNT, exceptionsToHandle);
    }

    /**
     * Retries a void action up to a given number of times if it fails due to specified exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "submit-button")
     * WebElement submitButton;
     *
     * public void clickSubmitButtonWithRetry() {
     *     retryVoidAction(() -> submitButton.click(), 3, ElementClickInterceptedException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The void action to execute.
     * @param retryCount         The number of times to retry the action.
     * @param exceptionsToHandle The exceptions upon which to retry the action.
     */
    @SafeVarargs
    public static void retryVoidAction(Runnable action, int retryCount, Class<? extends Exception>... exceptionsToHandle) {
        retryAction(() -> {
            action.run();
            return null; // For compatibility with Supplier<T>
        }, retryCount, exceptionsToHandle);
    }

    /**
     * Retries a BooleanSupplier action up to the default retry count if it fails due to specified exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "checkbox-id")
     * WebElement checkbox;
     *
     * public boolean isCheckboxSelectedWithRetry() {
     *     return ActionHandler.retryBooleanAction(() -> checkbox.isSelected(), NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The BooleanSupplier action to execute.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The boolean result of the action if it succeeds within the allowed retry attempts.
     */
    @SafeVarargs
    public static boolean retryBooleanAction(BooleanSupplier action, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action::getAsBoolean, DEFAULT_RETRY_COUNT, exceptionsToHandle);
    }

    /**
     * Retries a BooleanSupplier action with a specified number of retry attempts if the action fails due to certain exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "dynamic-element-id")
     * WebElement dynamicElement;
     *
     * public boolean waitForElementVisibilityWithRetry() {
     *     return ActionHandler.retryBooleanAction(() -> dynamicElement.isDisplayed(), 5, NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The BooleanSupplier action to execute.
     * @param retryCount         The number of retry attempts before giving up.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The boolean result of the action if it succeeds within the specified retry attempts.
     */
    @SafeVarargs
    public static boolean retryBooleanAction(BooleanSupplier action, int retryCount, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action::getAsBoolean, retryCount, exceptionsToHandle);
    }

    /**
     * Retries an IntSupplier action up to the default retry count if it fails due to specified exceptions.
     *
     * <pre>{@code
     * @FindBy(className = "item-class")
     * List<WebElement> items;
     *
     * public int getItemCountWithRetry() {
     *     return ActionHandler.retryIntAction(() -> items.size(), NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The IntSupplier action to execute.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The integer result of the action if it succeeds within the allowed retry attempts.
     */
    @SafeVarargs
    public static int retryIntAction(IntSupplier action, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action::getAsInt, DEFAULT_RETRY_COUNT, exceptionsToHandle);
    }

    /**
     * Retries an IntSupplier action with a specified number of retry attempts if the action fails due to certain exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "dynamic-content-id")
     * WebElement dynamicContent;
     *
     * public int calculateDynamicValueWithRetry() {
     *     return ActionHandler.retryIntAction(() -> Integer.parseInt(dynamicContent.getText()), 3, NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The IntSupplier action to execute.
     * @param retryCount         The number of retry attempts before giving up.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The integer result of the action if it succeeds within the specified retry attempts.
     */
    @SafeVarargs
    public static int retryIntAction(IntSupplier action, int retryCount, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action::getAsInt, retryCount, exceptionsToHandle);
    }

    /**
     * Retries a DoubleSupplier action up to the default retry count if it fails due to specified exceptions.
     *
     * <pre>{@code
     * @FindBy(css = "div.price")
     * List<WebElement> prices;
     *
     * public double calculateAveragePriceWithRetry() {
     *     return ActionHandler.retryDoubleAction(() -> prices.stream().mapToDouble(element -> Double.parseDouble(element.getText())).average().orElse(Double.NaN), NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The DoubleSupplier action to execute.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The double result of the action if it succeeds within the allowed retry attempts.
     */
    @SafeVarargs
    public static double retryDoubleAction(DoubleSupplier action, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action::getAsDouble, DEFAULT_RETRY_COUNT, exceptionsToHandle);
    }

    /**
     * Retries a DoubleSupplier action with a specified number of retry attempts if the action fails due to certain exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "dynamic-value")
     * WebElement dynamicValue;
     *
     * public double extractValueWithRetry() {
     *     return ActionHandler.retryDoubleAction(() -> Double.parseDouble(dynamicValue.getAttribute("data-value")), 5, NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The DoubleSupplier action to execute.
     * @param retryCount         The number of retry attempts before giving up.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The double result of the action if it succeeds within the specified retry attempts.
     */
    @SafeVarargs
    public static double retryDoubleAction(DoubleSupplier action, int retryCount, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action::getAsDouble, retryCount, exceptionsToHandle);
    }

    /**
     * Retries a Supplier&lt;String&gt; action up to the default retry count if it fails due to specified exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "dynamic-text")
     * WebElement dynamicText;
     *
     * public String getTextWithRetry() {
     *     return ActionHandler.retryStringAction(() -> dynamicText.getText(), NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The Supplier&lt;String&gt; action to execute.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The String result of the action if it succeeds within the allowed retry attempts.
     */
    @SafeVarargs
    public static String retryStringAction(Supplier<String> action, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action, DEFAULT_RETRY_COUNT, exceptionsToHandle);
    }

    /**
     * Retries a Supplier&lt;String&gt; action with a specified number of retry attempts if the action fails due to certain exceptions.
     *
     * <pre>{@code
     * @FindBy(id = "conditional-text")
     * WebElement conditionalText;
     *
     * public String getConditionalTextWithRetry() {
     *     return ActionHandler.retryStringAction(() -> {
     *         String text = conditionalText.getText();
     *         if (text.equals("Expected Value")) {
     *             return text;
     *         } else {
     *             throw new StaleElementReferenceException("Text not as expected");
     *         }
     *     }, 3, NoSuchElementException.class, StaleElementReferenceException.class);
     * }
     * }</pre>
     *
     * @param action             The Supplier&lt;String&gt; action to execute.
     * @param retryCount         The number of retry attempts before giving up.
     * @param exceptionsToHandle The exceptions that, if thrown, will trigger a retry of the action.
     * @return The String result of the action if it succeeds within the specified retry attempts.
     */
    @SafeVarargs
    public static String retryStringAction(Supplier<String> action, int retryCount, Class<? extends Exception>... exceptionsToHandle) {
        return retryAction(action, retryCount, exceptionsToHandle);
    }

    /**
     * Determines if the caught exception is among the exceptions that should be handled
     * (and thus retried) by the retry logic.
     *
     * <p>This method iterates through the provided array of exceptions to handle and checks if the caught exception
     * is an instance of them. It is utilized internally by the public retry methods
     * to decide whether an exception thrown during an attempted action should lead to a retry or be propagated immediately.</p>
     *
     * @param exception          The exception that was caught during the execution of the action.
     * @param exceptionsToHandle An array of exceptions that are eligible for retry logic.
     * @return true if the exception is one of the handled types; false otherwise.
     */
    private static boolean isExceptionHandled(Exception exception, Class<? extends Exception>[] exceptionsToHandle) {
        return Arrays.stream(exceptionsToHandle).anyMatch(handledException -> handledException.isInstance(exception));
    }
}
