package org.autoutils.driver;

import org.autoutils.driver.chrome.factory.ChromeDriverFactory;
import org.autoutils.driver.edge.factory.EdgeDriverFactory;
import org.autoutils.driver.exception.InvalidBrowserException;
import org.autoutils.driver.exception.InvalidBrowserOptionsException;
import org.autoutils.driver.firefox.factory.FirefoxDriverFactory;
import org.autoutils.driver.internetexplorer.factory.InternetExplorerDriverFactory;
import org.autoutils.driver.safari.factory.SafariDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariOptions;
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
    public WebDriver getDriver(Browser browserType, Object options) {
        WebDriver driver;

        switch (browserType) {
            case CHROME:
                if (options instanceof ChromeOptions chromeOptions) {
                    driver = new ChromeDriverFactory().create(chromeOptions, null);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Chrome. Expected ChromeOptions.");
                }
                break;

            case FIREFOX:
                if (options instanceof FirefoxOptions firefoxOptions) {
                    driver = new FirefoxDriverFactory().create(firefoxOptions, null);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Firefox. Expected FirefoxOptions.");
                }
                break;

            case EDGE:
                if (options instanceof EdgeOptions edgeOptions) {
                    driver = new EdgeDriverFactory().create(edgeOptions, null);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Edge. Expected EdgeOptions.");
                }
                break;

            case INTERNET_EXPLORER:
                if (options instanceof InternetExplorerOptions ieOptions) {
                    driver = new InternetExplorerDriverFactory().create(ieOptions, null);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for IE. Expected InternetExplorerOptions.");
                }
                break;

            case SAFARI:
                if (options instanceof SafariOptions safariOptions) {
                    driver = new SafariDriverFactory().create(safariOptions, null);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Safari. Expected SafariOptions.");
                }
                break;

            default:
                throw new InvalidBrowserException("Unsupported browser: " + browserType);
        }

        DriverSessionManager.registerDriver(driver);
        LOGGER.debug("{} driver initialized successfully.", browserType);
        return driver;
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
