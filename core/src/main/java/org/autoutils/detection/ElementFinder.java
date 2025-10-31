package org.autoutils.detection;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Provides methods to find and ensure the visibility of web elements on a page.
 * It abstracts common patterns of element retrieval, including waiting for their presence or visibility,
 * directly finding elements, and ensuring a collection of elements are all visible.
 */
public class ElementFinder {

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Constructor for ElementFinder.
     *
     * @param driver the WebDriver instance to be used for finding elements.
     * @param wait   the WebDriverWait instance to be used for waiting for elements.
     */
    public ElementFinder(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Waits for the presence of an element located by the given locator and returns it.
     *
     * @param locator The locator to find the element.
     * @return The located WebElement.
     */
    public WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for the presence of an element located by the given locator with a custom wait time and returns it.
     *
     * @param locator  The locator to find the element.
     * @param waitTime The custom duration to wait.
     * @return The located WebElement.
     */
    public WebElement findElement(By locator, Duration waitTime) {
        WebDriverWait customWait = new WebDriverWait(driver, waitTime);
        return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Immediately finds and returns a single element located by the given locator.
     *
     * @param locator The locator to find the element.
     * @return The found WebElement.
     */
    public WebElement getElement(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Immediately finds and returns a list of elements located by the given locator.
     *
     * @param locator The locator to find elements.
     * @return A list of WebElements.
     */
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Waits until all elements in the provided list are visible and returns them.
     * This method is useful for ensuring that elements dynamically loaded onto the page are visible.
     *
     * @param elements A list of WebElements to check for visibility.
     * @return A list of visible WebElements.
     */
    public List<WebElement> getElements(List<WebElement> elements) {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (TimeoutException e) {
            return Collections.emptyList();
        }
    }
}