package org.autoutils.driver;

import io.appium.java_client.AppiumDriver;

public class AppiumDriverManager implements Driver<AppiumDriver> {

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
            System.out.println("AppiumDriver session closed.");
        }
    }

    @Override
    public void quitDriver() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            appiumDriver = null;
            System.out.println("AppiumDriver session quit.");
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (appiumDriver == null) {
            throw new IllegalStateException("AppiumDriver not initialized.");
        }
    }
}
