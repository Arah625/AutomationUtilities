package org.autoutils.detection;

import org.autoutils.detection.wait.element.WaitForAllElementsFacade;
import org.autoutils.detection.wait.element.WaitForFirstElementFacade;
import org.autoutils.detection.wait.locator.WaitForAllLocatorsFacade;
import org.autoutils.detection.wait.locator.WaitForFirstLocatorFacade;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewElementVisibilityHandler {

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final FluentWait<WebDriver> defaultFluentWait;
    private final WaitForFirstLocatorFacade waitForFirstLocatorFacade;
    private final WaitForAllLocatorsFacade waitForAllLocatorsFacade;
    private final WaitForFirstElementFacade waitForFirstElementFacade;
    private final WaitForAllElementsFacade waitForAllElementsFacade;


    /**
     * Constructor for ElementVisibilityHandler.
     *
     * @param driver            The WebDriver instance to be used for visibility checks.
     * @param webDriverWait     The WebDriverWait instance to be used for waiting for elements.
     * @param defaultFluentWait The default FluentWait instance provided by the target project.
     */
    public NewElementVisibilityHandler(WebDriver driver, WebDriverWait webDriverWait, FluentWait<WebDriver> defaultFluentWait) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.defaultFluentWait = defaultFluentWait;
        this.waitForFirstLocatorFacade = new WaitForFirstLocatorFacade(driver, webDriverWait, defaultFluentWait);
        this.waitForAllLocatorsFacade = new WaitForAllLocatorsFacade(driver, webDriverWait, defaultFluentWait);
        this.waitForFirstElementFacade = new WaitForFirstElementFacade(driver, webDriverWait, defaultFluentWait);
        this.waitForAllElementsFacade = new WaitForAllElementsFacade(driver, webDriverWait, defaultFluentWait);
    }

    public WaitForFirstLocatorFacade getWaitForFirstLocatorFacade() {
        return waitForFirstLocatorFacade;
    }

    public WaitForAllLocatorsFacade getWaitForAllLocatorsFacade() {
        return waitForAllLocatorsFacade;
    }

    public WaitForFirstElementFacade getWaitForFirstElementFacade() {
        return waitForFirstElementFacade;
    }

    public WaitForAllElementsFacade getWaitForAllElementsFacade() {
        return waitForAllElementsFacade;
    }
}
