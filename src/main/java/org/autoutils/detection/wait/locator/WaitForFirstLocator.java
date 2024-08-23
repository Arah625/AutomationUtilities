package org.autoutils.detection.wait.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Provides utility methods for waiting until at least one of the specified locators
 * points to a visible element on the web page.
 * These methods are beneficial when a test scenario requires proceeding only
 * after any one of multiple potential elements, identified by their locators,
 * becomes visible—useful for dealing with dynamic content or multiple possible outcomes.
 */
public class WaitForFirstLocator {

    private final WebDriver webDriver;
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitForFirstLocator.class);

    /**
     * Constructor for WaitForFirstLocator.
     *
     * @param webDriver The WebDriver instance to be used for locating elements.
     */
    public WaitForFirstLocator(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Waits for any one of the specified locators to point to a visible element within the web page.
     * Iterates through a list of locators, checking each for element visibility, and returns true
     * at the first occurrence of a visible element. This method is particularly useful for scenarios where
     * the appearance of any one of several elements indicates readiness to proceed with the test flow.
     *
     * @param fluentWait   The {@link FluentWait} instance, customized with timeout and polling settings.
     * @param locatorsList A list of {@link By} locators to be checked for element visibility.
     * @return True if any one of the elements identified by the locators becomes visible, false otherwise.
     * <p>
     * Example usage:
     * <pre>{@code
     * List<By> messageLocators = Arrays.asList(successMessageLocator, errorMessageLocator, warningMessageLocator);
     * boolean isAnyMessageVisible = WaitForFirstLocator.waitForVisibilityOfFirstLocator(fluidWait, messageLocators);
     * }</pre>
     */
    public boolean waitForVisibilityOfFirstLocator(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        try {
            return fluentWait.until(driver -> {
                for (By locator : locatorsList) {
                    List<WebElement> elements = this.webDriver.findElements(locator);
                    if (!elements.isEmpty()) {
                        for (WebElement element : elements) {
                            if (element.isDisplayed()) {
                                LOGGER.info("Element found and is visible: {}", locator);
                                return true;
                            }
                        }
                    }
                }
                return false;
            });
        } catch (TimeoutException timeoutException) {
            LOGGER.error("Exception while waiting for visibility: ", timeoutException);
            return false;
        }
    }

    /**
     * Iteratively checks for the visibility of elements identified by each provided
     * locator, applying a dynamic polling mechanism within the specified total wait
     * time. This method sequentially verifies the visibility of each element identified
     * by the locators, suitable for scenarios where elements are expected to become
     * visible individually or in a particular sequence within dynamic user interfaces.
     * <p>
     * Each polling interval checks all specified locators, removing any whose
     * identified elements are confirmed visible from the list of locators yet to be
     * checked. This process continues until either all elements identified by the
     * specified locators are confirmed visible or the total wait time elapses.
     * <p>
     * Utilizing varargs simplifies the specification of locators to be checked,
     * making this method especially useful for ensuring the sequential visibility
     * of elements identified by a known set of locators without needing to construct
     * a list.
     *
     * @param fluentWait The {@link FluentWait} instance specifying the wait
     *                   conditions, including timeout and polling intervals.
     * @param locators   Varargs of {@link By} locators to be checked for visibility
     *                   sequentially.
     * @return True if each element identified by the provided locators becomes
     * visible within the total wait period, false otherwise.
     * <p>
     * Example usage in a test case:
     * <pre>{@code
     * public boolean areFormValidationMessagesVisible() {
     *     return WaitForAllLocators.waitForEachLocatorVisibility(WebDriverSetup.getInstance().getFluentWait(),
     *                                                             message1Locator, message2Locator, message3Locator);
     * }
     * }</pre>
     * <p>
     * This method is a valuable tool for handling dynamic UI elements that appear
     * sequentially, contributing to more precise and reliable tests.
     */
    public boolean waitForVisibilityOfFirstLocator(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForVisibilityOfFirstLocator(fluentWait, Arrays.asList(locators));
    }

    /**
     * Waits for any one of the specified locators to point to an element that becomes invisible on the web page.
     * Iterates through a list of locators, checking each for element invisibility, and returns true
     * at the first occurrence of an invisible element. This method is useful for scenarios where
     * the disappearance of any one of several elements indicates readiness to proceed with the test flow.
     *
     * @param fluentWait   The {@link FluentWait} instance, customized with timeout and polling settings.
     * @param locatorsList A list of {@link By} locators to be checked for element invisibility.
     * @return True if any one of the elements identified by the locators becomes invisible, false otherwise.
     * <p>
     * Example usage:
     * <pre>{@code
     * List<By> messageLocators = Arrays.asList(successMessageLocator, errorMessageLocator, warningMessageLocator);
     * boolean isAnyMessageInvisible = WaitForFirstLocator.waitForInvisibilityOfFirstLocator(fluidWait, messageLocators);
     * }</pre>
     */
    public boolean waitForInvisibilityOfFirstLocator(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        try {
            return fluentWait.until(driver -> {
                for (By locator : locatorsList) {
                    List<WebElement> elements = this.webDriver.findElements(locator);
                    if (!elements.isEmpty()) {
                        for (WebElement element : elements) {
                            if (!element.isDisplayed()) {
                                LOGGER.info("Element is now invisible: {}", locator);
                                return true;
                            }
                        }
                    } else {
                        LOGGER.info("Element not found, assumed invisible: {}", locator);
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException timeoutException) {
            LOGGER.error("Exception while waiting for invisibility: ", timeoutException);
            return false;
        }
    }

    /**
     * Waits for the invisibility of at least one element identified by the specified locators. This version of the method
     * uses varargs for easier inline specification of locators to check for invisibility. This is particularly
     * useful when you have a small set of locators and you want the test to proceed as soon as any one of
     * the identified elements becomes invisible on the page.
     *
     * @param fluentWait  The {@link FluentWait} configured with specific timeout and polling interval, adapting
     *                    the wait strategy to the dynamic nature of web elements' invisibility.
     * @param locators Varargs array of {@link By} instances to check for invisibility. This flexibility
     *                    allows for direct passing of multiple locators without needing a list.
     * @return True if any one of the elements identified by the provided locators becomes invisible within the wait time defined in fluentWait;
     * false if none of the elements become invisible within this period.
     * <p>
     * Example usage in a test case:
     * <pre>{@code
     * boolean isAnyElementInvisible = WaitForFirstLocator.waitForInvisibilityOfFirstLocator(
     *     WebDriverSetup.getInstance().getFluentWait(), submitButtonLocator, cancelButtonLocator, errorMessageLocator);
     * assertTrue(isAnyElementInvisible, "Expected at least one element to be invisible but all were visible.");
     * }</pre>
     * This method is a versatile tool in handling conditional flows within a page, where the invisibility
     * of different elements could lead to different paths in the test execution.
     */
    public boolean waitForInvisibilityOfFirstLocator(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForInvisibilityOfFirstLocator(fluentWait, Arrays.asList(locators));
    }

}