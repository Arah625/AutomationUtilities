package org.autoutils.detection.wait.element;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A utility class offering methods to wait for specific visibility conditions of web elements within a webpage.
 * It supports waiting for all given elements to become visible simultaneously, ensuring elements are ready for user interactions.
 * This class is especially useful in scenarios such as dynamic content loading where elements like password hints appear sequentially.
 * <p>
 * Utilizes {@link FluentWait} for customizable wait strategies, including setting timeouts and polling intervals,
 * while providing graceful exception handling capabilities.
 */
public class WaitForAllElements {

    private final WebDriver webDriver;
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitForAllElements.class);

    /**
     * Constructor for WaitForAllElements.
     *
     * @param webDriver The WebDriver instance to be used for locating elements.
     */
    public WaitForAllElements(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    /**
     * Waits for all elements in a given list to become visible at the same time. Each element's visibility is checked
     * at every polling interval within the total wait time specified.
     * This method is crucial for scenarios where multiple
     * elements, such as password hints that appear one after another, need to be confirmed visible before proceeding.
     *
     * @param fluentWait      The {@link FluentWait} instance specifying the wait conditions.
     * @param webElementsList The list of {@link WebElement} to check for simultaneous visibility.
     * @return True if all specified elements are visible within the wait period, false if at least one is not.
     * <p>
     * Usage example within a page object method:
     * <pre>{@code
     * public boolean arePasswordHintsVisible() {
     *     List<WebElement> passwordHints = Arrays.asList(hint1, hint2, hint3);
     *     return WaitForAllElements.waitForVisibilityOfAllElements(WebDriverSetup.getInstance().getFluentWait(),
     *     passwordHints);
     * }
     * }</pre>
     * <p>
     * This can be utilized in tests as follows:
     * <pre>{@code
     * Assert.assertTrue(registerPage.arePasswordHintsVisible(), "Password hints are not visible as expected.");
     * }</pre>
     */
    public boolean waitForVisibilityOfAllElements(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
        Set<String> notVisibleOrFoundElementsDescriptions = ConcurrentHashMap.newKeySet();

        boolean allElementsVisible = fluentWait.until(driver -> {
            AtomicBoolean allVisible = new AtomicBoolean(true);

            webElementsList.forEach(element -> {
                try {
                    if (element.isDisplayed()) {
                        notVisibleOrFoundElementsDescriptions.remove(element.toString());
                    } else {
                        notVisibleOrFoundElementsDescriptions.add(element.toString());
                        allVisible.set(false);
                    }
                } catch (NoSuchElementException e) {
                    notVisibleOrFoundElementsDescriptions.add(element.toString());
                    allVisible.set(false);
                }
            });

            return allVisible.get();
        });

        if (!allElementsVisible) {
            LOGGER.error("Timeout reached. Not all elements were visible before the timeout.");
            notVisibleOrFoundElementsDescriptions.forEach(description ->
                    LOGGER.error("Element not visible or not found: {}", description));
            return false;
        }

        return true;
    }

    /**
     * Waits for all given web elements to become visible at the same time. This method is a convenient overload,
     * allowing for direct input of multiple {@link WebElement} instances without needing to create a list explicitly.
     * This method is crucial for scenarios where multiple elements,
     * such as password hints that appear one after another, need to be confirmed visible before proceeding.
     *
     * @param fluentWait  The {@link FluentWait} instance specifying the wait conditions,
     *                    such as timeout and polling frequency.
     * @param webElements Varargs array of {@link WebElement} instances to be checked for visibility.
     * @return True if all specified elements become visible within the wait period, false otherwise.
     * <p>
     * Usage example within a page object method for a set of known elements:
     * <pre>{@code
     * public boolean areNavigationButtonsVisible() {
     *     return WaitForAllElements.waitForAllElementsVisibility(WebDriverSetup.getInstance().getFluentWait(),
     *     nextButton, backButton, homeButton);
     * }
     * }</pre>
     * <p>
     * This method can be utilized in tests to verify the visibility of a predefined group of elements:
     * <pre>{@code
     * Assert.assertTrue(page.areNavigationButtonsVisible(), "Navigation buttons are not visible as expected.");
     * }</pre>
     */
    public boolean waitForVisibilityOfAllElements(FluentWait<WebDriver> fluentWait, WebElement... webElements) {
        return waitForVisibilityOfAllElements(fluentWait, Arrays.asList(webElements));
    }

    /**
     * Iteratively waits for each element in the provided list to become visible on the webpage,
     * checking at regular intervals within the total specified wait time.
     * The method continuously loops through the list, checking for the visibility of each element.
     * Once an element is found to be visible, it is removed from the list. This loop repeats at each polling
     * interval until either the list is empty (indicating all elements have been found and are visible) or the timeout
     * is reached. It is particularly useful for scenarios where elements are expected to appear
     * and disappear sequentially, such as dynamic form validation messages or tutorial steps
     * that are displayed one after another.
     *
     * @param fluentWait      The {@link FluentWait} instance specifying the wait conditions,
     *                        including timeout and polling frequency.
     * @param webElementsList A list of {@link WebElement} instances to be checked for visibility. The checking
     *                        is performed in sequence and at each polling interval.
     * @return True if all elements in the list become visible within the specified wait period,
     * false if any element remains invisible by the time the timeout is reached.
     * <p>
     * Usage example within a page object method for sequentially appearing elements:
     * <pre>{@code
     * public boolean areSequentialHintsVisible() {
     *     List<WebElement> hints = Arrays.asList(hint1, hint2, hint3);
     *     return WaitForAllElements.waitForEachElementVisibility(WebDriverSetup.getInstance().getFluentWait(), hints);
     * }
     * }</pre>
     * <p>
     * This method can be effectively utilized in tests to verify that sequential elements are visible as expected:
     * <pre>{@code
     * Assert.assertTrue(tutorialPage.areSequentialHintsVisible(), "Sequential hints are not visible as expected.");
     * }</pre>
     */
    public boolean waitForEachElementToBeVisibleOnce(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
        List<WebElement> remainingElements = new ArrayList<>(webElementsList);

        boolean allElementsVisible = fluentWait.until(driver -> {
            Iterator<WebElement> iterator = remainingElements.iterator();
            while (iterator.hasNext()) {
                WebElement webElement = iterator.next();
                try {
                    if (webElement.isDisplayed()) {
                        LOGGER.info("Element found and visible: {}", webElement);
                        iterator.remove();
                    }
                } catch (NoSuchElementException noSuchElementException) {
                    iterator.remove();
                }
            }
            return remainingElements.isEmpty();
        });

        if (!allElementsVisible) {
            LOGGER.error("Timeout reached. The following elements were not visible:");
            remainingElements.forEach(webElement -> LOGGER.error("Element not visible or not found: {}", webElement));
            return false;
        }
        return true;
    }


    /**
     * Iteratively checks for the visibility of each provided {@link WebElement},
     * utilizing varargs for input convenience. This method applies a dynamic polling
     * mechanism within the specified total wait time, continuously checking each
     * element's visibility at regular intervals. It's designed for scenarios where
     * elements may become visible one after another, making it ideal for dynamic
     * content loading or interactive tutorials.
     * <p>
     * Each polling interval checks all specified elements, removing any that are
     * confirmed visible from the list of elements yet to be checked. This process
     * continues until either all specified elements are confirmed visible or the
     * total wait time elapses.
     * <p>
     * Utilizing varargs simplifies the specification of elements to be checked,
     * making this method particularly useful for validating the visibility of a
     * known set of elements without the need to construct a list.
     *
     * @param fluentWait  The {@link FluentWait} instance specifying the wait
     *                    conditions, including timeout and polling intervals.
     * @param webElements Varargs of {@link WebElement} to be checked for visibility
     *                    sequentially.
     * @return True if each specified element becomes visible within the total wait
     * period, false otherwise.
     * <p>
     * Example usage in a page object method:
     * <pre>{@code
     * public boolean areInteractiveTutorialStepsVisible() {
     *     return WaitForAllElements.waitForEachElementToBeVisibleOnce(
     *     WebDriverSetup.getInstance().getFluentWait(), step1, step2, step3);
     * }
     * }</pre>
     * <p>
     * This method facilitates the handling of dynamic UI elements that appear
     * sequentially, enhancing test accuracy and reliability.
     */
    public boolean waitForEachElementToBeVisibleOnce(FluentWait<WebDriver> fluentWait,
                                                            WebElement... webElements) {
        return waitForEachElementToBeVisibleOnce(fluentWait, Arrays.asList(webElements));
    }
}