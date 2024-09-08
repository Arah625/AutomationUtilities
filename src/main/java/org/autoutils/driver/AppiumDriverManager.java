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

    @Override
    public void setDriver(AppiumDriver driver) {
        this.appiumDriver = driver;
    }

    @Override
    public void closeDriver() {
        if (appiumDriver != null) {
            appiumDriver.close();
            appiumDriver = null;
        }
    }

    @Override
    public void quitDriver() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            appiumDriver = null;
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (appiumDriver == null) {
            throw new IllegalStateException("AppiumDriver not initialized.");
        }
    }
}
