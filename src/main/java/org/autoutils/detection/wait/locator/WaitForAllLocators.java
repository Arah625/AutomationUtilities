package org.autoutils.detection.wait.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
class WaitForAllLocators {

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
     * Waits for all elements identified by the given list of locators to become visible simultaneously.
     *
     * <p>This method checks all elements identified by the locators at regular intervals as defined by the {@link FluentWait} instance.
     * The method ensures that all elements are visible at the same polling interval within the specified timeout period.
     * If any element is not visible during a polling interval, the method will continue checking until either all elements
     * are visible simultaneously or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Simultaneous Visibility:</b> All elements identified by the locators must be visible at the same polling interval for the method to return {@code true}.</li>
     *     <li><b>Logging on Failure:</b> If not all elements become visible simultaneously, the method logs which locators' elements were not visible.</li>
     * </ul>
     *
     * <p>If all elements become visible at the same time within the timeout period, the method returns {@code true}.
     * If any element fails to become visible at the same time as the others, the method returns {@code false}.</p>
     *
     * @param fluentWait   The {@link FluentWait} instance specifying the wait conditions.
     * @param locatorsList The list of {@link By} locators identifying the elements to check for visibility.
     * @return True if all specified elements become visible at the same time within the wait period, false if at least one does not.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllSectionsVisibleSimultaneously() {
     *     List<By> sections = Arrays.asList(headerLocator, footerLocator, contentLocator);
     *     return locatorVisibilityHandler.waitForVisibilityOfAllLocators(fluentWait, sections);
     * }
     * }</pre>
     */
    boolean waitForVisibilityOfAllLocators(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        List<By> remainingLocators = new ArrayList<>(locatorsList);

        boolean allVisible = fluentWait.until(driver -> {
            boolean allElementsVisible = true;

            for (By locator : remainingLocators) {
                try {
                    WebElement element = webDriver.findElement(locator);
                    if (!element.isDisplayed()) {
                        allElementsVisible = false;
                    }
                } catch (NoSuchElementException e) {
                    allElementsVisible = false;
                }
            }

            // If all elements are visible simultaneously, return true
            return allElementsVisible;
        });

        if (!allVisible) {
            LOGGER.error("Timeout reached. Not all elements were visible at the same time.");
            remainingLocators.forEach(locator -> LOGGER.error("Element not visible for locator: {}", locator));
            return false;
        }
        return true;
    }

    /**
     * Waits for all elements identified by the given locators to become visible simultaneously.
     *
     * <p>This method is a convenient overload, allowing for direct input of multiple {@link By} locators without needing to create a list.
     * It checks all elements identified by the locators at each polling interval, ensuring that all elements are visible
     * simultaneously during the same polling interval. The method continues to check all elements together until
     * they are all visible simultaneously or the timeout is reached.</p>
     *
     * <p>Key Points:</p>
     * <ul>
     *     <li><b>Simultaneous Visibility Check:</b> All elements identified by the locators must be visible at the same time in one polling interval.</li>
     *     <li><b>No Premature Removal:</b> Elements are not removed from the checking list once visible. The method ensures
     *     that all elements remain visible simultaneously.</li>
     * </ul>
     *
     * <p>If all elements are visible at the same time within the timeout period, the method returns {@code true}.
     * If any element fails to become visible during the same polling interval before the timeout, the method returns {@code false}
     * and logs the locators whose elements were not visible.</p>
     *
     * @param fluentWait The {@link FluentWait} instance specifying the wait conditions, including timeout and polling intervals.
     * @param locators   Varargs array of {@link By} locators identifying the elements to check for simultaneous visibility.
     * @return True if all specified elements become visible simultaneously within the wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllRequiredSectionsVisible() {
     *     return locatorVisibilityHandler.waitForVisibilityOfAllLocators(fluentWait, headerLocator, footerLocator, contentLocator);
     * }
     * }</pre>
     */
    boolean waitForVisibilityOfAllLocators(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForVisibilityOfAllLocators(fluentWait, Arrays.asList(locators));
    }

    /**
     * Iteratively waits for each locator in the provided list to have its corresponding element become visible at least once.
     *
     * <p>This method checks each element identified by the locators in the list sequentially. At each polling interval, it checks whether
     * the first unchecked locator's element is visible. If the element is visible, the locator is removed from the list, and the method
     * proceeds to the next locator in the next polling interval. This continues until either all locators' elements are confirmed
     * to have been visible at least once or the timeout period is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Visibility Checks:</b> The method checks one locator per polling interval. Once an element is confirmed visible, its locator is removed from the list and no longer checked.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be visible at the same time. The method only requires that each locator's element has been visible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException), it is assumed the element was not visible during that polling interval.</li>
     * </ul>
     *
     * <p>If all locators' elements are confirmed visible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become visible at least once before the timeout, the method returns {@code false} and logs
     * the locators whose elements were not confirmed visible.</p>
     *
     * @param fluentWait   The {@link FluentWait} instance specifying the wait conditions.
     * @param locatorsList The list of {@link By} locators identifying the elements to check for visibility.
     * @return True if all elements identified by the locators in the list are confirmed to have been visible at least once within the specified wait period, false if any element is not confirmed visible by the time the timeout is reached.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areSequentialHintsVisible() {
     *     List<By> hintsLocators = Arrays.asList(hint1Locator, hint2Locator, hint3Locator);
     *     return locatorVisibilityHandler.waitForEachLocatorToBeVisibleOnce(fluentWait, hintsLocators);
     * }
     * }</pre>
     */
   boolean waitForEachLocatorToBeVisibleOnce(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        List<By> notVisibleLocators = new ArrayList<>(locatorsList);

        boolean allElementsVisible = fluentWait.until(driver -> {
            Iterator<By> iterator = notVisibleLocators.iterator();
            while (iterator.hasNext()) {
                By locator = iterator.next();
                try {
                    WebElement webElement = webDriver.findElement(locator);
                    if (webElement.isDisplayed()) {
                        LOGGER.info("Element found and visible for locator: {}", locator);
                        iterator.remove();
                    }
                } catch (NoSuchElementException noSuchElementException) {
                    // Element not found, continue checking remaining locators
                }
            }
            return notVisibleLocators.isEmpty();
        });

        if (!allElementsVisible) {
            LOGGER.error("Timeout reached. The following locators' elements were not confirmed visible:");
            notVisibleLocators.forEach(locator -> LOGGER.error("Element not visible or not found for locator: {}", locator));
            return false;
        }
        return true;
    }

    /**
     * Iteratively waits for each element identified by the provided locators to become visible at least once.
     *
     * <p>This method checks the visibility of each element identified by the locators sequentially at each polling interval within the specified
     * total wait time. As soon as an element is confirmed visible, its locator is removed from the list of locators yet to be checked.
     * The method continues until all elements have been confirmed visible at least once, or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Visibility Checks:</b> The method checks each locator one by one at each polling interval.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be visible at the same time. The method only requires that each element identified by the locator is visible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException),
     *     it is assumed that the element was not visible during that polling interval.</li>
     * </ul>
     *
     * <p>If all locators' elements are confirmed visible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become visible at least once before the timeout, the method returns {@code false} and logs
     * the locators whose elements were not confirmed visible.</p>
     *
     * @param fluentWait The {@link FluentWait} instance specifying the wait conditions.
     * @param locators   Varargs array of {@link By} locators identifying the elements to check for visibility sequentially.
     * @return True if each element identified by the provided locators becomes visible at least once within the total wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areSequentialHintsVisible() {
     *     return locatorVisibilityHandler.waitForEachLocatorToBeVisibleOnce(fluentWait, hint1Locator, hint2Locator, hint3Locator);
     * }
     * }</pre>
     */
   boolean waitForEachLocatorToBeVisibleOnce(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForEachLocatorToBeVisibleOnce(fluentWait, Arrays.asList(locators));
    }

    /**
     * Waits for all elements identified by the given list of locators to become invisible simultaneously.
     *
     * <p>This method checks all elements identified by the locators at regular intervals as defined by the {@link FluentWait} instance.
     * The method ensures that all elements are invisible at the same polling interval within the specified timeout period.
     * If any element is still visible during a polling interval, the method will continue checking until either all elements
     * are invisible simultaneously or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Simultaneous Invisibility:</b> All elements identified by the locators must be invisible at the same polling interval for the method to return {@code true}.</li>
     *     <li><b>Logging on Failure:</b> If not all elements become invisible simultaneously, the method logs which locators' elements were not invisible.</li>
     * </ul>
     *
     * <p>If all elements become invisible at the same time within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible at the same time as the others, the method returns {@code false}.</p>
     *
     * @param fluentWait   The {@link FluentWait} instance specifying the wait conditions.
     * @param locatorsList The list of {@link By} locators identifying the elements to check for invisibility.
     * @return True if all specified elements become invisible at the same time within the wait period, false if at least one does not.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllSectionsGoneSimultaneously() {
     *     List<By> sections = Arrays.asList(headerLocator, footerLocator, contentLocator);
     *     return locatorInvisibilityHandler.waitForInvisibilityOfAllLocators(fluentWait, sections);
     * }
     * }</pre>
     */
   boolean waitForInvisibilityOfAllLocators(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        Set<String> notInvisibleOrFoundElementsDescriptions = ConcurrentHashMap.newKeySet();

        boolean allElementsInvisibleAtSameTime = fluentWait.until(driver -> {
            boolean allInvisible = true;

            // Iterate over all locators to ensure their elements are invisible at the same time
            for (By locator : locatorsList) {
                try {
                    WebElement element = webDriver.findElement(locator);
                    if (element.isDisplayed()) {
                        notInvisibleOrFoundElementsDescriptions.add(locator.toString());
                        allInvisible = false;
                    }
                } catch (NoSuchElementException e) {
                    // If the element is not found, it's considered invisible
                    notInvisibleOrFoundElementsDescriptions.remove(locator.toString());
                }
            }

            // If all elements are invisible, clear the list of descriptions
            if (allInvisible) {
                notInvisibleOrFoundElementsDescriptions.clear();
            }

            return allInvisible;
        });

        if (!allElementsInvisibleAtSameTime) {
            LOGGER.error("Timeout reached. Not all elements were invisible at the same time.");
            notInvisibleOrFoundElementsDescriptions.forEach(description ->
                    LOGGER.error("Element not invisible or still present: {}", description));
            return false;
        }

        return true;
    }

    /**
     * Waits for all elements identified by the given locators to become invisible simultaneously.
     *
     * <p>This method is a convenient overload, allowing for direct input of multiple {@link By} locators without needing to create a list.
     * It checks all elements identified by the locators at each polling interval, ensuring that all elements are invisible
     * simultaneously during the same polling interval. The method continues to check all elements together until
     * they are all invisible simultaneously or the timeout is reached.</p>
     *
     * <p>Key Points:</p>
     * <ul>
     *     <li><b>Simultaneous Invisibility Check:</b> All elements identified by the locators must be invisible at the same time in one polling interval.</li>
     *     <li><b>No Premature Removal:</b> Elements are not removed from the checking list once invisible. The method ensures
     *     that all elements remain invisible simultaneously.</li>
     * </ul>
     *
     * <p>If all elements are invisible at the same time within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible during the same polling interval before the timeout, the method returns {@code false}
     * and logs the locators whose elements were not invisible.</p>
     *
     * @param fluentWait The {@link FluentWait} instance specifying the wait conditions, including timeout and polling intervals.
     * @param locators   Varargs array of {@link By} locators identifying the elements to check for simultaneous invisibility.
     * @return True if all specified elements become invisible simultaneously within the wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areAllRequiredSectionsGone() {
     *     return locatorInvisibilityHandler.waitForInvisibilityOfAllLocators(fluentWait, headerLocator, footerLocator, contentLocator);
     * }
     * }</pre>
     */
    boolean waitForInvisibilityOfAllLocators(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForInvisibilityOfAllLocators(fluentWait, Arrays.asList(locators));
    }

    /**
     * Iteratively waits for each locator in the provided list to have its corresponding element become invisible at least once.
     *
     * <p>This method checks each element identified by the locators in the list sequentially. At each polling interval, it checks whether
     * the first unchecked locator's element is invisible. If the element is invisible, the locator is removed from the list, and the method
     * proceeds to the next locator in the next polling interval. This continues until either all locators' elements are confirmed
     * to have been invisible at least once or the timeout period is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Invisibility Checks:</b> The method checks one locator per polling interval. Once an element is confirmed invisible, its locator is removed from the list and no longer checked.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be invisible at the same time. The method only requires that each locator's element has been invisible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException), it is assumed the element was already invisible during that polling interval.</li>
     * </ul>
     *
     * <p>If all locators' elements are confirmed invisible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible at least once before the timeout, the method returns {@code false} and logs
     * the locators whose elements were not confirmed invisible.</p>
     *
     * @param fluentWait   The {@link FluentWait} instance specifying the wait conditions.
     * @param locatorsList The list of {@link By} locators identifying the elements to check for invisibility.
     * @return True if all elements identified by the locators in the list are confirmed to have been invisible at least once within the specified wait period, false if any element is not confirmed invisible by the time the timeout is reached.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areSequentialHintsGone() {
     *     List<By> hintsLocators = Arrays.asList(hint1Locator, hint2Locator, hint3Locator);
     *     return locatorInvisibilityHandler.waitForEachLocatorToBeInvisibleOnce(fluentWait, hintsLocators);
     * }
     * }</pre>
     */
   boolean waitForEachLocatorToBeInvisibleOnce(FluentWait<WebDriver> fluentWait, List<By> locatorsList) {
        List<By> remainingLocators = new ArrayList<>(locatorsList);

        boolean allElementsInvisible = fluentWait.until(driver -> {
            Iterator<By> iterator = remainingLocators.iterator();
            while (iterator.hasNext()) {
                By locator = iterator.next();
                try {
                    WebElement webElement = webDriver.findElement(locator);
                    if (!webElement.isDisplayed()) {
                        LOGGER.info("Element found and now invisible for locator: {}", locator);
                        iterator.remove();
                    }
                } catch (NoSuchElementException noSuchElementException) {
                    // Element not found, so it's considered invisible
                    iterator.remove();
                }
            }
            return remainingLocators.isEmpty();
        });

        if (!allElementsInvisible) {
            LOGGER.error("Timeout reached. The following locators' elements were not confirmed invisible:");
            remainingLocators.forEach(locator -> LOGGER.error("Element still visible or not found for locator: {}", locator));
            return false;
        }
        return true;
    }

    /**
     * Iteratively waits for each specified locator's element to become invisible at least once, using varargs for input convenience.
     *
     * <p>This method checks the invisibility of each element identified by the locators sequentially at each polling interval within the specified
     * total wait time. As soon as an element is confirmed invisible, its locator is removed from the list of locators yet to be checked.
     * The method continues until all elements have been confirmed invisible at least once, or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Invisibility Checks:</b> The method checks each locator one by one at each polling interval.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be invisible at the same time. The method only requires that each locator's element is invisible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException),
     *     it is assumed that the element is already invisible during that polling interval.</li>
     * </ul>
     *
     * <p>If all locators' elements are confirmed invisible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible at least once before the timeout, the method returns {@code false} and logs
     * the locators whose elements were not confirmed invisible.</p>
     *
     * @param fluentWait The {@link FluentWait} instance specifying the wait conditions.
     * @param locators   Varargs array of {@link By} locators identifying the elements to check for invisibility sequentially.
     * @return True if each element identified by the provided locators becomes invisible at least once within the total wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areFormValidationMessagesGone() {
     *     return locatorInvisibilityHandler.waitForEachLocatorToBeInvisibleOnce(fluentWait, message1Locator, message2Locator, message3Locator);
     * }
     * }</pre>
     */
   boolean waitForEachLocatorToBeInvisibleOnce(FluentWait<WebDriver> fluentWait, By... locators) {
        return waitForEachLocatorToBeInvisibleOnce(fluentWait, Arrays.asList(locators));
    }
}