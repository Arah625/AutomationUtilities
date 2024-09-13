package org.autoutils.action.element;

import org.autoutils.detection.ElementFinder;
import org.autoutils.retry.ActionHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClickableElement extends JavaScriptExecutorBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickableElement.class);

    protected final WebDriverWait webDriverWait;
    protected final ElementFinder elementFinder;

    protected static final int DEFAULT_RETRY_COUNT = 2;

    protected ClickableElement(RemoteWebDriver remoteWebDriver, WebDriverWait webDriverWait) {
        super(remoteWebDriver);
        this.webDriverWait = webDriverWait;
        this.elementFinder = new ElementFinder(remoteWebDriver, webDriverWait);
    }

    /**
     * Clicks on the element with retry logic. Uses the default retry count.
     *
     * @param webElement the WebElement to be clicked
     */
    public void clickElement(WebElement webElement) {
        clickElement(webElement, DEFAULT_RETRY_COUNT);
    }

    /**
     * Clicks on the element with a user-defined retry count.
     *
     * @param webElement the WebElement to be clicked
     * @param retryCount the number of retry attempts
     */
    public void clickElement(WebElement webElement, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Performing click on element {}", webElement);
            webElement.click();
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    /**
     * Clicks on the element located by the given locator with retry logic. Uses the default retry count.
     *
     * @param locator the {@code By} locator to find the element to be clicked
     */
    public void clickElement(By locator) {
        clickElement(locator, DEFAULT_RETRY_COUNT);
    }

    /**
     * Clicks on the element located by the given locator with a user-defined retry count.
     *
     * @param locator the {@code By} locator to find the element to be clicked
     * @param retryCount the number of retry attempts
     */
    public void clickElement(By locator, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            WebElement webElement = elementFinder.findElement(locator);
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Performing click on element {}", webElement);
            webElement.click();
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void clickWithJavaScript(WebElement webElement) {
        clickWithJavaScript(webElement, DEFAULT_RETRY_COUNT);
    }

    /**
     * Clicks using JavaScript with retry logic.
     *
     * @param webElement the WebElement to be clicked
     * @param retryCount the number of retry attempts
     */
    public void clickWithJavaScript(WebElement webElement, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Performing JavaScript click on element {}", webElement);
            executeScript("arguments[0].click();", webElement);
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void clickWithJavaScript(By locator) {
        clickWithJavaScript(locator, DEFAULT_RETRY_COUNT);
    }

    public void clickWithJavaScript(By locator, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            WebElement webElement = elementFinder.findElement(locator);
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Performing JavaScript click on element {}", webElement);
            executeScript("arguments[0].click();", webElement);
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void scrollToCenterClickWithJavaScript(WebElement webElement) {
        scrollToCenterClickWithJavaScript(webElement, DEFAULT_RETRY_COUNT);
    }

    /**
     * Scrolls to center and clicks using JavaScript with retry logic.
     *
     * @param webElement the WebElement to be clicked
     * @param retryCount the number of retry attempts
     */
    public void scrollToCenterClickWithJavaScript(WebElement webElement, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Scrolling to center and performing JavaScript click on element {}", webElement);
            executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", webElement);
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void scrollToCenterClickWithJavaScript(By locator) {
        scrollToCenterClickWithJavaScript(locator, DEFAULT_RETRY_COUNT);
    }

    public void scrollToCenterClickWithJavaScript(By locator, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            WebElement webElement = elementFinder.findElement(locator);
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Scrolling to center and performing JavaScript click on element {}", webElement);
            executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", webElement);
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void scrollToCenterClickTriggerEventWithJavaScript(WebElement webElement) {
        scrollToCenterClickTriggerEventWithJavaScript(webElement, DEFAULT_RETRY_COUNT);
    }

    /**
     * Scrolls to center and triggers click event using JavaScript with retry logic.
     *
     * @param webElement the WebElement to be clicked
     * @param retryCount the number of retry attempts
     */
    public void scrollToCenterClickTriggerEventWithJavaScript(WebElement webElement, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Scrolling to center and triggering click event on element {}", webElement);
            executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].dispatchEvent(new Event('click'));", webElement);
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void scrollToCenterClickTriggerEventWithJavaScript(By locator) {
        scrollToCenterClickTriggerEventWithJavaScript(locator, DEFAULT_RETRY_COUNT);
    }

    public void scrollToCenterClickTriggerEventWithJavaScript(By locator, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            WebElement webElement = elementFinder.findElement(locator);
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Scrolling to center and triggering click event on element {}", webElement);
            executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].dispatchEvent(new Event('click'));", webElement);
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void clickWithActions(WebElement webElement) {
        clickWithActions(webElement, DEFAULT_RETRY_COUNT);
    }

    public void clickWithActions(WebElement webElement, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Performing Actions click on element {}", webElement);
            Actions actions = new Actions(driver);
            actions.moveToElement(webElement).click().perform();
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    public void clickWithActions(By locator) {
        clickWithActions(locator, DEFAULT_RETRY_COUNT);
    }

    public void clickWithActions(By locator, int retryCount) {
        ActionHandler.retryVoidAction(() -> {
            WebElement webElement = elementFinder.findElement(locator);
            waitForVisibilityAndClickability(webElement);
            LOGGER.debug("Performing Actions click on element {}", webElement);
            Actions actions = new Actions(driver);
            actions.moveToElement(webElement).click().perform();
        }, retryCount, StaleElementReferenceException.class, ElementClickInterceptedException.class);
    }

    /**
     * Waits for the element to be visible and clickable before clicking.
     *
     * @param webElement the WebElement to wait for
     */
    private void waitForVisibilityAndClickability(WebElement webElement) {
        LOGGER.debug("Waiting for element {} to be visible", webElement);
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        LOGGER.debug("Waiting for element {} to be clickable", webElement);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    private void waitForVisibilityAndClickability(By locator) {
        LOGGER.debug("Waiting for element located by {} to be visible", locator);
        WebElement webElement = elementFinder.findElement(locator);
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        LOGGER.debug("Waiting for element located by {} to be clickable", locator);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

}

