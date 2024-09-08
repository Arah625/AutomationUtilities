package org.autoutils.driver;

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

public class WebDriverManager implements Driver<WebDriver> {

    private WebDriver webDriver;
    private static final WebDriverManager INSTANCE = new WebDriverManager();

    private WebDriverManager() {
        // Private constructor to prevent instantiation
    }

    public static WebDriverManager getInstance() {
        return INSTANCE;
    }

    /**
     * Get WebDriver with default options.
     */
    public WebDriver getDriver() {
        ensureDriverInitialized();
        return webDriver;
    }

    /**
     * Get WebDriver with specific browser options (overloaded method).
     * This method accepts browser-specific options and initializes the driver with them.
     */
    public WebDriver getDriver(String browserType, Object options) {
        switch (browserType.toLowerCase()) {
            case "chrome":
                webDriver = new ChromeDriver((ChromeOptions) options);
                break;
            case "firefox":
                webDriver = new FirefoxDriver((FirefoxOptions) options);
                break;
            case "edge":
                webDriver = new EdgeDriver((EdgeOptions) options);
                break;
            case "ie":
                webDriver = new InternetExplorerDriver((InternetExplorerOptions) options);
                break;
            case "safari":
                webDriver = new SafariDriver((SafariOptions) options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }

        return webDriver;
    }

    @Override
    public void setDriver(WebDriver driver) {
        this.webDriver = driver;
    }

    @Override
    public void closeDriver() {
        if (webDriver != null) {
            webDriver.close();
            webDriver = null;
        }
    }

    @Override
    public void quitDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver not initialized.");
        }
    }
}
