package org.autoutils.visibility;

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
class WaitForAllElements {

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
     * Waits for all elements in a given list to become visible at the same time.
     *
     * <p>This method polls all elements in the provided list at regular intervals, as defined by the {@link FluentWait} instance.
     * The method ensures that all elements are visible simultaneously during the same polling interval within the specified timeout period.
     * If any element is not visible during the polling interval, the method will continue checking until either all elements are visible
     * at the same time or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Simultaneous Visibility:</b> All elements must be visible at the same polling interval for the method to return {@code true}.</li>
     *     <li><b>Logging on Failure:</b> If not all elements become visible simultaneously, the method logs which elements were not visible.</li>
     * </ul>
     *
     * <p>If all elements become visible at the same time within the timeout period, the method returns {@code true}.
     * If any element fails to become visible at the same time as the others, the method returns {@code false}.</p>
     *
     * @param fluentWait      The {@link FluentWait} instance specifying the wait conditions.
     * @param webElementsList The list of {@link WebElement} to check for visibility.
     * @return True if all specified elements become visible at the same time within the wait period, false if at least one does not.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean arePasswordHintsVisibleSimultaneously() {
     *     List<WebElement> passwordHints = Arrays.asList(hint1, hint2, hint3);
     *     return elementVisibilityHandler.waitForVisibilityOfAllElements(fluentWait, passwordHints);
     * }
     * }</pre>
     */
    boolean waitForVisibilityOfAllElements(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
        Set<String> elementsDescriptions = ConcurrentHashMap.newKeySet();

        boolean allElementsVisibleAtSameTime = fluentWait.until(driver -> {
            boolean allVisible = true;

            // Iterate over all elements to ensure they are visible at the same time
            for (WebElement element : webElementsList) {
                try {
                    if (!element.isDisplayed()) {
                        elementsDescriptions.add(element.toString());
                        allVisible = false;
                    }
                } catch (NoSuchElementException e) {
                    elementsDescriptions.add(element.toString());
                    allVisible = false;
                }
            }

            // If all elements are visible, clear the list of descriptions
            if (allVisible) {
                elementsDescriptions.clear();
            }

            return allVisible;
        });

        if (!allElementsVisibleAtSameTime) {
            LOGGER.error("Timeout reached. Not all elements were visible at the same time.");
            elementsDescriptions.forEach(description ->
                    LOGGER.error("Element not visible or not found at the same time: {}", description));
            return false;
        }

        return true;
    }

    /**
     * Waits for all specified web elements to become visible at the same time within the provided timeout period.
     *
     * <p>This method checks all specified elements at each polling interval, ensuring that all elements are visible
     * simultaneously during the same polling interval. The method continues to check all elements together until
     * they are all visible or the timeout is reached.</p>
     *
     * <p><b>Key Points:</b></p>
     * <ul>
     *     <li><b>Simultaneous Visibility Check:</b> All elements must be visible at the same time in one polling interval.
     *     If any element is not visible during the same polling interval, the method will continue to check until the timeout.</li>
     *     <li><b>No Premature Removal:</b> Elements are not removed from the checking list once visible. The method ensures
     *     that all elements remain visible simultaneously.</li>
     * </ul>
     *
     * <p>If all elements are visible at the same time within the timeout period, the method returns {@code true}.
     * If any element fails to become visible during the same polling interval before the timeout, the method returns {@code false}
     * and logs the elements that were not visible.</p>
     *
     * @param fluentWait  The {@link FluentWait} instance specifying the wait conditions, including timeout and polling intervals.
     * @param webElements Varargs array of {@link WebElement} instances to be checked for simultaneous visibility.
     * @return True if all specified elements become visible simultaneously within the wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areNavigationButtonsVisible() {
     *     return elementVisibilityHandler.waitForVisibilityOfAllElements(fluentWait, nextButton, backButton, homeButton);
     * }
     * }</pre>
     */
    boolean waitForVisibilityOfAllElements(FluentWait<WebDriver> fluentWait, WebElement... webElements) {
        return waitForVisibilityOfAllElements(fluentWait, Arrays.asList(webElements));
    }

    /**
     * Iteratively waits for each element in the provided list to become visible at least once.
     *
     * <p>This method checks each element in the list sequentially. At each polling interval, it checks whether
     * the first unchecked element is visible. If the element is visible, it is removed from the list, and the method
     * moves on to the next element in the next polling interval. This continues until either all elements are confirmed
     * to have been visible at least once or the timeout period is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Visibility Checks:</b> The method checks one element per polling interval. Once an element is confirmed visible, it is removed from the list and no longer checked.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be visible at the same time. The method only requires that each element has been visible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException), it is assumed the element was not visible during that polling interval.</li>
     * </ul>
     *
     * <p>If all elements are confirmed visible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become visible at least once before the timeout, the method returns {@code false} and logs
     * the elements that were not confirmed visible.</p>
     *
     * @param fluentWait      The {@link FluentWait} instance specifying the wait conditions.
     * @param webElementsList The list of {@link WebElement} instances to be checked for visibility.
     * @return True if all elements in the list are confirmed to have been visible at least once within the specified wait period, false if any element is not confirmed visible by the time the timeout is reached.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areSequentialHintsVisible() {
     *     List<WebElement> hints = Arrays.asList(hint1, hint2, hint3);
     *     return elementVisibilityHandler.waitForEachElementToBeVisibleOnce(fluentWait, hints);
     * }
     * }</pre>
     */
    boolean waitForEachElementToBeVisibleOnce(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
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
     * Iteratively waits for each specified web element to become visible at least once, using varargs for input convenience.
     *
     * <p>This method checks the visibility of each element sequentially at each polling interval within the specified
     * total wait time. As soon as an element is confirmed visible, it is removed from the list of elements yet to be checked.
     * The method continues until all elements have been confirmed visible at least once, or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Visibility Checks:</b> The method checks each element one by one at each polling interval.
     *     Once an element is confirmed to be visible, it is removed from the list and no longer checked.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be visible at the same time. The method only requires that each element is visible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException),
     *     it is assumed that the element was not visible during that polling interval.</li>
     * </ul>
     *
     * <p>If all elements are confirmed visible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become visible at least once before the timeout, the method returns {@code false} and logs
     * the elements that were not confirmed visible.</p>
     *
     * @param fluentWait  The {@link FluentWait} instance specifying the wait conditions.
     * @param webElements Varargs array of {@link WebElement} to be checked for visibility sequentially.
     * @return True if each specified element becomes visible at least once within the total wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areInteractiveTutorialStepsVisible() {
     *     return elementVisibilityHandler.waitForEachElementToBeVisibleOnce(fluentWait, step1, step2, step3);
     * }
     * }</pre>
     */
    boolean waitForEachElementToBeVisibleOnce(FluentWait<WebDriver> fluentWait,
                                              WebElement... webElements) {
        return waitForEachElementToBeVisibleOnce(fluentWait, Arrays.asList(webElements));
    }

    /**
     * Waits for all elements in a given list to become invisible simultaneously.
     *
     * <p>This method polls each element in the provided list at regular intervals, as defined by the {@link FluentWait} instance.
     * At each polling interval, it checks whether all remaining elements are invisible. The method continues checking until either
     * all elements are confirmed invisible at the same time or the specified timeout period is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Concurrent Invisibility Checks:</b> The method checks all remaining elements at each polling interval. If any element is still visible, the method keeps checking until all are confirmed invisible.</li>
     *     <li><b>Simultaneous Invisibility Required:</b> All elements must be confirmed invisible at the same time for the method to return {@code true}.</li>
     * </ul>
     *
     * <p>If all elements become invisible within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible before the timeout, the method returns {@code false} and logs the elements that were not confirmed invisible.</p>
     *
     * @param fluentWait      The {@link FluentWait} instance specifying the wait conditions.
     * @param webElementsList The list of {@link WebElement} to check for invisibility.
     * @return True if all specified elements become invisible simultaneously within the wait period, false if at least one does not.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean arePasswordHintsGone() {
     *     List<WebElement> passwordHints = Arrays.asList(hint1, hint2, hint3);
     *     return elementInvisibilityHandler.waitForInvisibilityOfAllElements(fluentWait, passwordHints);
     * }
     * }</pre>
     */
    boolean waitForInvisibilityOfAllElements(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
        Set<String> notInvisibleOrFoundElementsDescriptions = ConcurrentHashMap.newKeySet();

        boolean allElementsInvisible = fluentWait.until(driver -> {
            AtomicBoolean allInvisible = new AtomicBoolean(true);

            webElementsList.forEach(element -> {
                try {
                    if (element.isDisplayed()) {
                        notInvisibleOrFoundElementsDescriptions.add(element.toString());
                        allInvisible.set(false);
                    } else {
                        notInvisibleOrFoundElementsDescriptions.remove(element.toString());
                    }
                } catch (NoSuchElementException e) {
                    // Element is not present, so it's considered invisible.
                    notInvisibleOrFoundElementsDescriptions.remove(element.toString());
                }
            });

            return allInvisible.get();
        });

        if (!allElementsInvisible) {
            LOGGER.error("Timeout reached. Not all elements were invisible before the timeout.");
            notInvisibleOrFoundElementsDescriptions.forEach(description ->
                    LOGGER.error("Element not invisible or still present: {}", description));
            return false;
        }

        return true;
    }

    /**
     * Waits for all specified web elements to become invisible simultaneously.
     *
     * <p>This method is a convenient overload, allowing for direct input of multiple {@link WebElement} instances without needing to create a list explicitly.
     * It checks all remaining elements at each polling interval, and the method continues until all elements are invisible simultaneously or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Concurrent Invisibility Checks:</b> The method checks all remaining elements at each polling interval. If any element is still visible, the method keeps checking until all are confirmed invisible.</li>
     *     <li><b>Simultaneous Invisibility Required:</b> All elements must be confirmed invisible at the same time for the method to return {@code true}.</li>
     * </ul>
     *
     * <p>If all elements become invisible within the timeout, the method returns {@code true}.
     * If any element fails to become invisible before the timeout, the method returns {@code false} and logs the elements that were not confirmed invisible.</p>
     *
     * @param fluentWait  The {@link FluentWait} instance specifying the wait conditions.
     * @param webElements Varargs array of {@link WebElement} instances to be checked for invisibility.
     * @return True if all specified elements become invisible simultaneously within the wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areNavigationButtonsGone() {
     *     return elementInvisibilityHandler.waitForInvisibilityOfAllElements(fluentWait, nextButton, backButton, homeButton);
     * }
     * }</pre>
     */
    boolean waitForInvisibilityOfAllElements(FluentWait<WebDriver> fluentWait, WebElement... webElements) {
        return waitForInvisibilityOfAllElements(fluentWait, Arrays.asList(webElements));
    }

    /**
     * Iteratively waits for each element in the provided list to become invisible at least once.
     *
     * <p>This method checks each element in the list sequentially. At each polling interval, it checks whether
     * the first unchecked element is invisible. If the element is invisible, it is removed from the list, and the method
     * proceeds to the next element in the next polling interval. This continues until either all elements are confirmed
     * to have been invisible at least once or the timeout period is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Invisibility Checks:</b> The method checks one element per polling interval. Once an element is confirmed invisible, it is removed from the list and no longer checked.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be invisible at the same time. The method only requires that each element has been invisible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException), it is assumed the element is already invisible.</li>
     * </ul>
     *
     * <p>If all elements are confirmed invisible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible at least once before the timeout, the method returns {@code false} and logs
     * the elements that were not confirmed invisible.</p>
     *
     * @param fluentWait      The {@link FluentWait} instance specifying the wait conditions.
     * @param webElementsList The list of {@link WebElement} instances to be checked for invisibility.
     * @return True if all elements in the list are confirmed to have been invisible at least once within the specified wait period, false if any element is not confirmed invisible by the time the timeout is reached.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areSequentialHintsGone() {
     *     List<WebElement> hints = Arrays.asList(hint1, hint2, hint3);
     *     return elementInvisibilityHandler.waitForEachElementToBeInvisibleOnce(fluentWait, hints);
     * }
     * }</pre>
     */
    boolean waitForEachElementToBeInvisibleOnce(FluentWait<WebDriver> fluentWait, List<WebElement> webElementsList) {
        List<WebElement> remainingElements = new ArrayList<>(webElementsList);

        boolean allElementsInvisible = fluentWait.until(driver -> {
            Iterator<WebElement> iterator = remainingElements.iterator();
            while (iterator.hasNext()) {
                WebElement webElement = iterator.next();
                try {
                    if (!webElement.isDisplayed()) {
                        LOGGER.info("Element found and now invisible: {}", webElement);
                        iterator.remove();
                    }
                } catch (NoSuchElementException noSuchElementException) {
                    // Element not found, so it's considered invisible
                    iterator.remove();
                }
            }
            return remainingElements.isEmpty();
        });

        if (!allElementsInvisible) {
            LOGGER.error("Timeout reached. The following elements were not confirmed invisible:");
            remainingElements.forEach(webElement -> LOGGER.error("Element still visible or not found: {}", webElement));
            return false;
        }
        return true;
    }

    /**
     * Iteratively waits for each specified web element to become invisible at least once, using varargs for input convenience.
     *
     * <p>This method checks the invisibility of each element sequentially at each polling interval within the specified
     * total wait time. As soon as an element is confirmed invisible, it is removed from the list of elements yet to be checked.
     * The method continues until all elements have been confirmed invisible at least once, or the timeout is reached.</p>
     *
     * <p>Key points:</p>
     * <ul>
     *     <li><b>Sequential Invisibility Checks:</b> The method checks each element one by one at each polling interval.
     *     Once an element is confirmed to be invisible, it is removed from the list and no longer checked.</li>
     *     <li><b>No Simultaneous Requirement:</b> The elements do not need to be invisible at the same time. The method only requires that each element is invisible at least once during the polling intervals.</li>
     *     <li><b>Graceful Handling:</b> If an element is not found during a check (e.g., due to NoSuchElementException),
     *     it is assumed that the element is already invisible.</li>
     * </ul>
     *
     * <p>If all elements are confirmed invisible at least once within the timeout period, the method returns {@code true}.
     * If any element fails to become invisible at least once before the timeout, the method returns {@code false} and logs
     * the elements that were not confirmed invisible.</p>
     *
     * @param fluentWait  The {@link FluentWait} instance specifying the wait conditions.
     * @param webElements Varargs array of {@link WebElement} to be checked for invisibility sequentially.
     * @return True if each specified element becomes invisible at least once within the total wait period, false otherwise.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * public boolean areInteractiveTutorialStepsGone() {
     *     return elementInvisibilityHandler.waitForEachElementToBeInvisibleOnce(fluentWait, step1, step2, step3);
     * }
     * }</pre>
     */
    boolean waitForEachElementToBeInvisibleOnce(FluentWait<WebDriver> fluentWait, WebElement... webElements) {
        return waitForEachElementToBeInvisibleOnce(fluentWait, Arrays.asList(webElements));
    }
}