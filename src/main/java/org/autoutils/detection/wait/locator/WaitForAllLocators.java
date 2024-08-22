package org.autoutils.detection.wait.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A utility class offering methods to wait for specific visibility conditions of locators within a webpage.
 * It supports waiting for all given locators to point to elements that become visible simultaneously,
 * ensuring the elements are ready for user interactions.
 * This class is particularly useful in scenarios such as dynamic content loading where elements identified
 * by these locators appear sequentially.
 * <p>
 * Utilizes {@link FluentWait} for customizable wait strategies, including setting timeouts and polling intervals,
 * while providing graceful exception handling capabilities.
 */
public class WaitForAllLocators {

    private final WebDriver webDriver;
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitForAllLocators.class);

    /**
     * Constructor for WaitForAllLocators.
     *
     * @param webDriver The WebDriver instance to be used for locating elements.
     */
    public WaitForAllLocators(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Waits for all elements identified by the given list of locators to become visible at the same time.
     * Each element's visibility is checked at every polling interval within the total wait time specified.
     * This method is essential for scenarios where multiple elements, such as different sections of a dynamic page,
     * need to be confirmed visible before proceeding.
     *
     * @param fluentWait   The {@link FluentWait} instance specifying the wait conditions.
     * @param locatorsList The list of {@link By} locators to check for simultaneous visibility.
     * @return True if all specified elements are visible within the wait period, false if at least one is not.
     * <p>
     * Example usage within a page object method:
     * <pre>{@code
     * List<By> dynamicSections = Arrays.asList(section1Locator, section2Locator, section3Locator);
     * boolean areAllSectionsVisible = WaitForAllLocators.waitForVisibilityOfAllLocators(
     * WebDriverSetup.getInstance().getFluentWait(), dynamicSections);
     * }</pre>
     * <p>
     * This can be utilized in tests as follows:
     * <pre>{@code
     * Assert.assertTrue(page.areAllSectionsVisible(), "Not all dynamic sections are visible as expected.");
     * }</pre>
     */
    public boolean waitForVisibilityOfAllLocators(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        List<By> remainingLocators = new ArrayList<>(locatorsList);

        boolean allVisible = fluentWait.until(driver -> {
            Iterator<By> iterator = remainingLocators.iterator();
            while (iterator.hasNext()) {
                By locator = iterator.next();
                try {
                    WebElement webElement = webDriver.findElement(locator);
                    if (webElement.isDisplayed()) {
                        iterator.remove();
                    }
                } catch (NoSuchElementException e) {
                    LOGGER.warn("Element not found for locator: {}", locator, e);
                }
            }
            return remainingLocators.isEmpty();
        });

        if (!allVisible) {
            LOGGER.warn("Timeout reached. The following locators' elements were not visible:");
            remainingLocators.forEach(locator -> LOGGER.warn("Locator: {}", locator));
        }
        return allVisible;
    }

    /**
     * Waits for all elements identified by the given locators to become visible at the same time.
     * This method is a convenient overload, allowing for direct input of multiple {@link By} locators
     * without the need to explicitly create a list. It's essential for scenarios where the simultaneous
     * visibility of multiple elements is required for the test to proceed, such as when waiting for
     * different parts of a page to load completely.
     *
     * @param fluentWait The {@link FluentWait} instance specifying the wait conditions,
     *                   including timeout and polling frequency.
     * @param locators   Varargs array of {@link By} locators identifying the elements to check for visibility.
     * @return True if all elements identified by the locators become visible within the wait period, false otherwise.
     * <p>
     * Usage example within a page object method:
     * <pre>{@code
     * public boolean areAllRequiredSectionsVisible() {
     *     return WaitForAllLocators.waitForVisibilityOfAllLocators(WebDriverSetup.getInstance().getFluentWait(),
     *                                                               headerLocator, footerLocator, contentLocator);
     * }
     * }</pre>
     * <p>
     * This method can be utilized in tests to ensure that all critical sections of a page are visible:
     * <pre>{@code
     * Assert.assertTrue(page.areAllRequiredSectionsVisible(), "Not all sections are visible as expected.");
     * }</pre>
     */
    public boolean waitForVisibilityOfAllLocators(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForVisibilityOfAllLocators(fluentWait, Arrays.asList(locators));
    }

    /**
     * Iteratively waits for each locator in the provided list to have its corresponding element
     * become visible on the webpage, checking at regular intervals within the total specified wait time.
     * The method continuously loops through the list of locators, checking for the visibility
     * of their corresponding elements.
     * Once an element is found to be visible, its locator is removed from the list. This loop repeats at each polling
     * interval until either the list is empty (indicating all elements identified by the locators have been found
     * and are visible) or the timeout is reached. It is particularly useful for scenarios where elements
     * identified by locators are expected to appear and disappear sequentially,
     * such as dynamic form validation messages or tutorial steps that are displayed one after another.
     *
     * @param fluentWait   The {@link FluentWait} instance specifying the wait conditions,
     *                     including timeout and polling frequency.
     * @param locatorsList A list of {@link By} locators to be checked for visibility. The checking
     *                     is performed in sequence and at each polling interval.
     * @return True if all elements identified by the locators in the list become visible within the specified
     * wait period, false if any element remains invisible by the time the timeout is reached.
     * <p>
     * Example usage within a page object method for sequentially appearing elements identified by locators:
     * <pre>{@code
     * public boolean areSequentialHintsVisible() {
     *     List<By> hintsLocators = Arrays.asList(hint1Locator, hint2Locator, hint3Locator);
     *     return WaitForAllLocators.waitForVisibilityOfEachLocator(
     *     WebDriverSetup.getInstance().getFluentWait(), hintsLocators);
     * }
     * }</pre>
     * <p>
     * This method can be effectively utilized in tests to verify that sequential elements
     * identified by locators are visible as expected:
     * <pre>{@code
     * Assert.assertTrue(tutorialPage.areSequentialHintsVisible(),
     * "Sequential hints identified by locators are not visible as expected.");
     * }</pre>
     */
    public boolean waitForEachLocatorToBeVisibleOnce(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        List<By> notVisibleLocators = new ArrayList<>(locatorsList);

        boolean allElementsVisible = fluentWait.until(driver -> {
            boolean allVisible = true;
            List<By> currentlyNotVisible = new ArrayList<>();

            for (By locator : locatorsList) {
                List<WebElement> elements = webDriver.findElements(locator);
                if (elements.isEmpty() || !elements.get(0).isDisplayed()) {
                    allVisible = false;
                    currentlyNotVisible.add(locator);
                }
            }
            notVisibleLocators.retainAll(currentlyNotVisible);
            return allVisible;
        });

        if (!allElementsVisible) {
            LOGGER.warn("Some elements were not visible: ");
            notVisibleLocators.forEach(locator -> LOGGER.warn("Element not found or not visible for locator: {}", locator));
        }
        return allElementsVisible;
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
     *     return WaitForAllLocators.waitForEachLocatorVisibility(
     *     WebDriverSetup.getInstance().getFluentWait(), message1Locator, message2Locator, message3Locator);
     * }
     * }</pre>
     * <p>
     * This method is a valuable tool for handling dynamic UI elements that appear
     * sequentially, contributing to more precise and reliable tests.
     */
    public boolean waitForEachLocatorToBeVisibleOnce(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForEachLocatorToBeVisibleOnce(fluentWait, Arrays.asList(locators));
    }
}