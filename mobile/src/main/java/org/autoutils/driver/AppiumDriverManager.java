package org.autoutils.driver;

import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppiumDriverManager implements Driver<AppiumDriver> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumDriverManager.class);

    private AppiumDriver appiumDriver;
    private static final AppiumDriverManager INSTANCE = new AppiumDriverManager();

    private AppiumDriverManager() {
        // Private constructor to prevent instantiation
    }

    public static AppiumDriverManager getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes the AppiumDriver with specific options and the Appium server URL.
     * This method should be overridden by platform-specific managers like AndroidDriverManager or IOSDriverManager.
     */
    public AppiumDriver getDriver() {
        ensureDriverInitialized();
        return appiumDriver;
    }

    @Override
    public void setDriver(AppiumDriver driver) {
        this.appiumDriver = driver;
    }

    @Override
    public void closeDriver() {
        if (appiumDriver != null) {
            appiumDriver.close();
            appiumDriver = null;
            LOGGER.debug("AppiumDriver session closed.");
        }
    }

    @Override
    public void quitDriver() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            appiumDriver = null;
            LOGGER.debug("AppiumDriver session quit.");
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (appiumDriver == null) {
            throw new IllegalStateException("AppiumDriver not initialized.");
        }
    }
}
