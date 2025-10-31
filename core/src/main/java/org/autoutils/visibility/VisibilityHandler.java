package org.autoutils.visibility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The `VisibilityHandler` class provides a centralized way to handle visibility and invisibility checks
 * for web elements identified by locators or WebElement instances. It leverages the WebDriver, WebDriverWait,
 * and FluentWait instances to perform these checks, ensuring that elements are present, visible, or invisible
 * based on the required conditions.
 *
 * <p>This class simplifies the process of managing different visibility and invisibility conditions
 * by offering separate handlers for locators and elements.</p>
 */
public class VisibilityHandler {

    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;
    private final FluentWait<WebDriver> fluentWait;
    private final LocatorVisibility locatorVisibility;
    private final LocatorInvisibility locatorInvisibility;
    private final ElementVisibility elementVisibility;
    private final ElementInvisibility elementInvisibility;

    /**
     * Constructs an instance of `VisibilityHandler` with the specified WebDriver, WebDriverWait, and FluentWait instances.
     *
     * @param webDriver     The WebDriver instance to interact with the browser.
     * @param webDriverWait The WebDriverWait instance for explicit wait conditions.
     * @param fluentWait    The FluentWait instance for more flexible waiting strategies.
     */
    public VisibilityHandler(WebDriver webDriver, WebDriverWait webDriverWait, FluentWait<WebDriver> fluentWait) {
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
        this.fluentWait = fluentWait;
        this.locatorVisibility = new LocatorVisibility(webDriver, webDriverWait, fluentWait);
        this.locatorInvisibility = new LocatorInvisibility(webDriver, webDriverWait, fluentWait);
        this.elementVisibility = new ElementVisibility(webDriver, webDriverWait, fluentWait);
        this.elementInvisibility = new ElementInvisibility(webDriver, webDriverWait, fluentWait);
    }

    /**
     * Returns the `LocatorVisibility` handler, which provides methods to check the visibility of elements identified by locators.
     *
     * @return An instance of `LocatorVisibility`.
     */
    public LocatorVisibility locatorVisibility() {
        return this.locatorVisibility;
    }

    /**
     * Returns the `LocatorInvisibility` handler, which provides methods to check the invisibility of elements identified by locators.
     *
     * @return An instance of `LocatorInvisibility`.
     */
    public LocatorInvisibility locatorInvisibility() {
        return this.locatorInvisibility;
    }

    /**
     * Returns the `ElementVisibility` handler, which provides methods to check the visibility of specific web elements.
     *
     * @return An instance of `ElementVisibility`.
     */
    public ElementVisibility elementVisibility() {
        return elementVisibility;
    }

    /**
     * Returns the `ElementInvisibility` handler, which provides methods to check the invisibility of specific web elements.
     *
     * @return An instance of `ElementInvisibility`.
     */
    public ElementInvisibility elementInvisibility() {
        return elementInvisibility;
    }
}
