package org.autoutils.detection.wait.element;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * This class provides utility methods to wait for the visibility of at least one element from a given collection.
 * Use these methods when a test needs to proceed only after any one of several possible elements becomes visible,
 * such as waiting for any one of multiple success messages or buttons that might appear as a result of an action.
 */
public class WaitForFirstElement {

    private final WebDriver webDriver;
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitForFirstElement.class);

    /**
     * Constructor for WaitForFirstElement.
     *
     * @param webDriver The WebDriver instance to be used for locating elements.
     */
    public WaitForFirstElement(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Waits for any one of the given web elements to become visible within the web page.
     * This method iterates through a list of web elements and checks their visibility,
     * returning true as soon as the first visible element is found. It's useful for conditions
     * where the visibility of any one element out of a set allows the test to proceed.
     *
     * @param fluentWait      The {@link FluentWait} instance, customized with timeout and polling settings.
     * @param webElementsList A list of {@link WebElement} objects to be checked for visibility.
     * @return True if any one of the elements becomes visible, false otherwise.
     * <p>
     * Example usage:
     * <pre>{@code
     * List<WebElement> options = Arrays.asList(option1, option2, option3);
     * boolean isVisible = WaitForAnyElement.waitForVisibilityOfAnyElement(fluidWait, options);
     * }</pre>
     */
    public boolean waitForVisibilityOfFirstElement(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
        return fluentWait.until(driver -> {
            for (WebElement element : webElementsList) {
                try {
                    if (element != null && element.isDisplayed()) {
                        LOGGER.info("Element is visible: {}", element);
                        return true;
                    }
                } catch (NoSuchElementException ignored) {
                    // Ignored since it's expected that some elements may not be found immediately.
                }
            }
            return false;
        });
    }

    /**
     * Waits for the visibility of at least one element from the specified elements. This version of the method
     * utilizes varargs for easier inline specification of elements to check for visibility. It's particularly
     * useful when you have a small, finite set of elements and you want the test to proceed as soon as any one of
     * these elements becomes visible on the page. This could be useful in scenarios where multiple outcomes are
     * possible and the appearance of any specific element indicates the test can move forward.
     *
     * @param fluentWait  The {@link FluentWait} configured with specific timeout and polling interval, adapting
     *                    the wait strategy to the dynamic nature of web elements' visibility.
     * @param webElements Varargs array of {@link WebElement} instances to check for visibility. This flexibility
     *                    allows for direct passing of multiple elements without needing a list.
     * @return True if any one of the provided elements becomes visible within the wait time defined in fluentWait;
     * false if none of the elements become visible within this period.
     * <p>
     * Example usage in a test case:
     * <pre>{@code
     * boolean isAnyElementVisible = WaitForAnyElement.waitForVisibilityOfAnyElement(
     *     WebDriverSetup.getInstance().getFluentWait(), submitButton, cancelButton, errorMessage);
     * assertTrue(isAnyElementVisible, "Expected at least one element to be visible but none were.");
     * }</pre>
     * This method is a versatile tool in handling conditional flows within a page, where the visibility
     * of different elements could lead to different paths in the test execution.
     */
    public boolean waitForVisibilityOfFirstElement(FluentWait<WebDriver> fluentWait, WebElement... webElements) {
        return waitForVisibilityOfFirstElement(fluentWait, Arrays.asList(webElements));
    }
}