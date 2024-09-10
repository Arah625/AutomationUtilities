package org.autoutils.driver;

import org.autoutils.driver.exception.InvalidBrowserException;
import org.autoutils.driver.exception.InvalidBrowserOptionsException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);

    /**
     * Get the WebDriver for web browsers (Chrome, Firefox, Edge, Safari).
     *
     * @param browser the browser type (chrome, firefox, edge, safari).
     * @param options the browser-specific capabilities/options.
     * @return the initialized WebDriver.
     * @throws InvalidBrowserOptionsException if the wrong options are passed.
     */
    public static WebDriver createWebDriver(String browser, Object options) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                if (options instanceof ChromeOptions chromeOptions) {
                    driver = new ChromeDriver(chromeOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Chrome. Expected ChromeOptions.");
                }
                break;

            case "firefox":
                if (options instanceof FirefoxOptions firefoxOptions) {
                    driver = new FirefoxDriver(firefoxOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Firefox. Expected FirefoxOptions.");
                }
                break;

            case "edge":
                if (options instanceof EdgeOptions edgeOptions) {
                    driver = new EdgeDriver(edgeOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Edge. Expected EdgeOptions.");
                }
                break;

            case "ie":
                if (options instanceof InternetExplorerOptions ieOptions) {
                    driver = new InternetExplorerDriver(ieOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for IE. Expected InternetExplorerOptions.");
                }
                break;

            case "safari":
                if (options instanceof SafariOptions safariOptions) {
                    driver = new SafariDriver(safariOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Safari. Expected SafariOptions.");
                }
                break;

            default:
                throw new InvalidBrowserException("Unsupported browser: " + browser);
        }

        DriverSessionManager.registerDriver(driver);
        LOGGER.debug("{} driver initialized successfully.", browser);
        return driver;
    }
}
