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
 */
public class ElementVisibilityHandler {

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WaitForAllElements waitForAllElements;
    private final WaitForFirstElement waitForFirstElement;
    private final WaitForAllLocators waitForAllLocators;
    private final WaitForFirstLocator waitForFirstLocator;

    /**
     * Constructor for ElementVisibilityHandler.
     *
     * @param driver        The WebDriver instance to be used for visibility checks.
     * @param webDriverWait The WebDriverWait instance to be used for waiting for elements.
     */
    public ElementVisibilityHandler(WebDriver driver, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.waitForAllElements = new WaitForAllElements(driver);
        this.waitForFirstElement = new WaitForFirstElement(driver);
        this.waitForAllLocators = new WaitForAllLocators(driver);
        this.waitForFirstLocator = new WaitForFirstLocator(driver);
    }

    /**
     * Waits for all specified web elements to become visible.
     *
     * @param webElements The web elements to check.
     * @return True if all elements are visible, false otherwise.
     */
    public boolean areAllElementsVisible(WebElement... webElements) {
        return waitForAllElements.waitForVisibilityOfAllElements(new FluentWait<>(driver), webElements);
    }

    /**
     * Waits for all specified web elements to become visible within a given polling interval.
     *
     * @param pollingInterval The interval to poll for element visibility.
     * @param webElements     The web elements to check.
     * @return True if all elements are visible, false otherwise.
     */
    public boolean areAllElementsVisible(Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllElements.waitForVisibilityOfAllElements(fluentWait, webElements);
    }

    /**
     * Waits for all specified web elements to become visible within a specified timeout and polling interval.
     *
     * @param timeout         The maximum time to wait for the elements to become visible.
     * @param pollingInterval The interval to poll for element visibility.
     * @param webElements     The web elements to check.
     * @return True if all elements are visible, false otherwise.
     */
    public boolean areAllElementsVisible(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllElements.waitForVisibilityOfAllElements(fluentWait, webElements);
    }

    /**
     * Waits for all web elements in the provided list to become visible.
     *
     * @param webElementsList The list of web elements to check.
     * @return True if all elements in the list are visible, false otherwise.
     */
    public boolean areAllElementsVisible(List<WebElement> webElementsList) {
        return waitForAllElements.waitForVisibilityOfAllElements(new FluentWait<>(driver), webElementsList);
    }

    /**
     * Checks if all elements in the given list are visible, considering a specific polling interval.
     *
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of web elements to check for visibility.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsVisible(Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllElements.waitForVisibilityOfAllElements(fluentWait, webElementsList);
    }

    /**
     * Checks if all elements in the given list are visible, considering both a custom timeout and polling interval.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of web elements to check for visibility.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsVisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllElements.waitForVisibilityOfAllElements(fluentWait, webElementsList);
    }

    /**
     * Checks if elements identified by the given locators are all visible.
     *
     * @param locators Locators identifying the elements to check.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsByLocatorsVisible(By... locators) {
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(new FluentWait<>(driver), locators);
    }

    /**
     * Checks if elements identified by the given locators are all visible, considering a specific polling interval.
     *
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsByLocatorsVisible(Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(fluentWait, locators);
    }

    /**
     * Checks if elements identified by the given locators are all visible,
     * considering both a custom timeout and polling interval.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locators        Locators identifying the elements to check.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsByLocatorsVisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(fluentWait, locators);
    }

    /**
     * Checks if all elements identified by the given locators are visible.
     *
     * @param locatorsList List of locators identifying the elements to check.
     * @return true if all elements become visible within the default wait time; false otherwise.
     */
    public boolean areAllElementsByLocatorsVisible(List<By> locatorsList) {
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(new FluentWait<>(driver), locatorsList);
    }

    /**
     * Checks if all elements identified by the given locators are visible, considering a specific polling interval.
     *
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    List of locators identifying the elements to check.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsByLocatorsVisible(Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(fluentWait, locatorsList);
    }

    /**
     * Checks if all elements identified by the given locators are visible,
     * considering both a custom timeout and polling interval.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param locatorsList    List of locators identifying the elements to check.
     * @return true if all elements become visible within the wait time; false otherwise.
     */
    public boolean areAllElementsByLocatorsVisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForAllLocators.waitForEachLocatorToBeVisibleOnce(fluentWait, locatorsList);
    }

    /**
     * Checks if any of the specified elements are visible.
     *
     * @param webElements Elements to check for visibility.
     * @return true if any of the elements are visible; false otherwise.
     */
    public boolean isAnyElementVisible(WebElement... webElements) {
        return waitForFirstElement.waitForVisibilityOfFirstElement(new FluentWait<>(driver), webElements);
    }

    /**
     * Checks if any of the specified elements are visible, considering a specific polling interval.
     *
     * @param pollingInterval Custom polling interval.
     * @param webElements     Elements to check for visibility.
     * @return true if any of the elements are visible within the wait time; false otherwise.
     */
    public boolean isAnyElementVisible(Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstElement.waitForVisibilityOfFirstElement(fluentWait, webElements);
    }

    /**
     * Checks if any of the specified elements are visible, considering both a custom timeout and polling interval.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElements     Elements to check for visibility.
     * @return true if any of the elements are visible within the wait time; false otherwise.
     */
    public boolean isAnyElementVisible(Duration timeout, Duration pollingInterval, WebElement... webElements) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstElement.waitForVisibilityOfFirstElement(fluentWait, webElements);
    }

    /**
     * Checks if any element from a list of elements is visible.
     *
     * @param webElementsList List of elements to check for visibility.
     * @return true if any of the elements in the list are visible; false otherwise.
     */
    public boolean isAnyElementVisible(List<WebElement> webElementsList) {
        return waitForFirstElement.waitForVisibilityOfFirstElement(new FluentWait<>(driver), webElementsList);
    }

    /**
     * Checks if any element from a list of elements is visible, considering a specific polling interval.
     *
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of elements to check for visibility.
     * @return true if any of the elements in the list are visible within the wait time; false otherwise.
     */
    public boolean isAnyElementVisible(Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstElement.waitForVisibilityOfFirstElement(fluentWait, webElementsList);
    }

    /**
     * Checks if any element from a list of elements is visible, considering both a custom timeout and polling interval.
     *
     * @param timeout         Custom timeout.
     * @param pollingInterval Custom polling interval.
     * @param webElementsList List of elements to check for visibility.
     * @return true if any of the elements in the list are visible within the wait time; false otherwise.
     */
    public boolean isAnyElementVisible(Duration timeout, Duration pollingInterval, List<WebElement> webElementsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstElement.waitForVisibilityOfFirstElement(fluentWait, webElementsList);
    }

    /**
     * Checks if any element identified by the given locators is visible.
     *
     * @param locators Locators identifying the elements to check.
     * @return true if any of the elements become visible within the wait time; false otherwise.
     */
    public boolean isAnyElementByLocatorVisible(By... locators) {
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(new FluentWait<>(driver), locators);
    }

    /**
     * Checks if any element identified by the given locators is visible, considering a specific polling interval.
     *
     * @param pollingInterval The custom polling interval to wait for element visibility.
     * @param locators        Varargs of locators identifying the elements to check.
     * @return True if any of the elements become visible within the specified polling interval; false otherwise.
     */
    public boolean isAnyElementByLocatorVisible(Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(fluentWait, locators);
    }

    /**
     * Checks if any element identified by the given locators is visible,
     * considering both a custom timeout and polling interval.
     *
     * @param timeout         The custom timeout duration to wait for element visibility.
     * @param pollingInterval The custom polling interval to wait between checks.
     * @param locators        Varargs of locators identifying the elements to check.
     * @return True if any of the elements become visible within the specified timeout and polling intervals;
     * false otherwise.
     */
    public boolean isAnyElementByLocatorVisible(Duration timeout, Duration pollingInterval, By... locators) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(fluentWait, locators);
    }

    /**
     * Checks if any element identified by locators in a list is visible.
     *
     * @param locatorsList A list of locators identifying the elements to check.
     * @return True if any of the elements become visible within the default wait time; false otherwise.
     */
    public boolean isAnyElementByLocatorVisible(List<By> locatorsList) {
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(new FluentWait<>(driver), locatorsList);
    }

    /**
     * Checks if any element identified by locators in a list is visible, considering a specific polling interval.
     *
     * @param pollingInterval The custom polling interval to wait for element visibility.
     * @param locatorsList    A list of locators identifying the elements to check.
     * @return True if any of the elements become visible within the specified polling interval; false otherwise.
     */
    public boolean isAnyElementByLocatorVisible(Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(fluentWait, locatorsList);
    }

    /**
     * Checks if any element identified by locators in a list is visible,
     * considering both a custom timeout and polling interval.
     *
     * @param timeout         The custom timeout duration to wait for element visibility.
     * @param pollingInterval The custom polling interval to wait between checks.
     * @param locatorsList    A list of locators identifying the elements to check.
     * @return True if any of the elements become visible within the specified timeout and polling intervals;
     * false otherwise.
     */
    public boolean isAnyElementByLocatorVisible(Duration timeout, Duration pollingInterval, List<By> locatorsList) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class);
        return waitForFirstLocator.waitForVisibilityOfFirstLocator(fluentWait, locatorsList);
    }

    /**
     * Checks the visibility of a specific web element.
     *
     * @param webElement The WebElement to check for visibility.
     * @return True if the element is visible; false otherwise.
     */
    public boolean isElementVisible(WebElement webElement) {
        try {
            return webDriverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks the visibility of a specific web element within a specified timeout.
     *
     * @param webElement The WebElement to check for visibility.
     * @param timeout    The maximum duration to wait for the element's visibility.
     * @return True if the element is visible within the specified timeout; false otherwise.
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
     * Verifies if a specific number of WebElements in a list are visible.
     *
     * @param webElementsList  A list of WebElements to check for visibility.
     * @param numberOfElements The number of elements expected to be visible.
     * @return True if the number of visible elements matches the expected number; false otherwise.
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
     * Checks if all WebElements in a list are visible.
     *
     * @param webElementsList A list of WebElements to check for visibility.
     * @return True if all elements in the list are visible; false otherwise.
     */
    public boolean isNumberOfElementsByWebElementsVisible(List<WebElement> webElementsList) {
        return isNumberOfElementsByWebElementsVisible(webElementsList, webElementsList.size());
    }

    /**
     * Verifies if a specific number of elements identified by locators are visible.
     *
     * @param locatorsList             A list of locators identifying elements.
     * @param expectedNumberOfElements The expected number of visible elements.
     * @return True if the number of visible elements matches the expected number; false otherwise.
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
     * Checks if all elements identified by a list of locators are visible.
     *
     * @param locatorsList A list of locators identifying elements.
     * @return True if all elements identified by the locators are visible; false otherwise.
     */
    public boolean isNumberOfElementsByLocatorsVisible(List<By> locatorsList) {
        return isNumberOfElementsByLocatorsVisible(locatorsList, locatorsList.size());
    }

    /**
     * Checks for the visibility of an element identified by a locator.
     *
     * @param locator The locator identifying the element.
     * @return True if the element is visible; false otherwise.
     */
    public boolean isElementVisible(By locator) {
        try {
            return webDriverWait.until(ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(locator), ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks for the visibility of an element identified by a locator within a specified timeout.
     *
     * @param locator The locator identifying the element.
     * @param timeout The maximum duration to wait for the element's visibility.
     * @return True if the element is visible within the specified timeout; false otherwise.
     */
    public boolean isElementVisible(By locator, Duration timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, timeout);
            return customWait.until(ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(locator), ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of a specific web element.
     *
     * @param webElement The WebElement to check for invisibility.
     * @return True if the element is invisible; false otherwise.
     */
    public boolean invisibilityOfElement(WebElement webElement) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOf(webElement));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Checks for the invisibility of an element identified by a locator.
     *
     * @param locator The locator identifying the element.
     * @return True if the element is invisible; false otherwise.
     */
    public boolean invisibilityOfElement(By locator) {
        try {
            return webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }
}