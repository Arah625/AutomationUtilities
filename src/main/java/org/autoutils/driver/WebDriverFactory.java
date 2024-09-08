package org.autoutils.driver;

import org.autoutils.driver.exception.InvalidBrowserException;
import org.autoutils.driver.exception.InvalidBrowserOptionsException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

public class WebDriverFactory {

    /**
     * Get the WebDriver for web browsers (Chrome, Firefox, Edge, Safari).
     *
     * @param browser the browser type (chrome, firefox, edge, safari).
     * @param options the browser-specific capabilities/options.
     * @return the initialized WebDriver.
     * @throws InvalidBrowserOptionsException if the wrong options are passed.
     */
    public static WebDriver getWebDriver(String browser, Object options) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                if (options instanceof ChromeOptions chromeOptions) {
                    driver = WebDriverManager.getInstance().getDriver("chrome", chromeOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Chrome. Expected ChromeOptions.");
                }
                break;

            case "firefox":
                if (options instanceof FirefoxOptions firefoxOptions) {
                    driver = WebDriverManager.getInstance().getDriver("firefox", firefoxOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Firefox. Expected FirefoxOptions.");
                }
                break;

            case "edge":
                if (options instanceof EdgeOptions edgeOptions) {
                    driver = WebDriverManager.getInstance().getDriver("edge", edgeOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Edge. Expected EdgeOptions.");
                }
                break;

            case "safari":
                if (options instanceof SafariOptions safariOptions) {
                    driver = WebDriverManager.getInstance().getDriver("safari", safariOptions);
                } else {
                    throw new InvalidBrowserOptionsException("Invalid options provided for Safari. Expected SafariOptions.");
                }
                break;

            default:
                throw new InvalidBrowserException("Unsupported browser: " + browser);
        }

        // Register the driver for session management
        DriverSessionManager.registerDriver(driver);
        return driver;
    }
}