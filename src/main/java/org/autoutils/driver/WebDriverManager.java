package org.autoutils.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverManager implements Driver<WebDriver> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverManager.class);

    private WebDriver webDriver;
    private static final WebDriverManager INSTANCE = new WebDriverManager();

    private WebDriverManager() {
        // Private constructor to prevent instantiation
    }

    public static WebDriverManager getInstance() {
        return INSTANCE;
    }

    /**
     * Get the initialized WebDriver.
     *
     * @return The WebDriver instance.
     */
    public WebDriver getDriver() {
        ensureDriverInitialized();
        return webDriver;
    }

    /**
     * Initializes WebDriver with specific browser options via the factory.
     *
     * @param browserType The type of browser (chrome, firefox, edge, etc.)
     * @param options     Browser-specific options
     * @return The initialized WebDriver instance
     */
    public WebDriver getDriver(String browserType, Object options) {
        webDriver = WebDriverFactory.createWebDriver(browserType, options);  // Delegate to WebDriverFactory
        return webDriver;
    }

    /**
     * Set the WebDriver instance for web tests.
     *
     * @param driver The WebDriver instance to be set.
     */
    @Override
    public void setDriver(WebDriver driver) {
        this.webDriver = driver;
    }

    /**
     * Closes the current WebDriver session.
     */
    @Override
    public void closeDriver() {
        if (webDriver != null) {
            webDriver.close();
            webDriver = null;
            LOGGER.debug("WebDriver session closed.");
        }
    }

    /**
     * Quits the current WebDriver session and releases resources.
     */
    @Override
    public void quitDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
            LOGGER.debug("WebDriver session quit.");
        }
    }

    /**
     * Ensure the WebDriver is initialized before use.
     */
    @Override
    public void ensureDriverInitialized() {
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver not initialized.");
        }
    }
}
