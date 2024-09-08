package org.autoutils.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;

public class AndroidDriverManager implements Driver<AndroidDriver> {

    private AndroidDriver androidDriver;
    private static final AndroidDriverManager INSTANCE = new AndroidDriverManager();

    private AndroidDriverManager() {
        // Private constructor to prevent instantiation
    }

    public static AndroidDriverManager getInstance() {
        return INSTANCE;
    }

    /**
     * Android-specific method to initialize the driver with UiAutomator2Options.
     *
     * @param options The UiAutomator2Options for Android.
     * @param appiumServerUrl The Appium server URL.
     * @return The initialized AndroidDriver instance.
     */
    public AndroidDriver getDriver(UiAutomator2Options options, URL appiumServerUrl) {
        androidDriver = new AndroidDriver(appiumServerUrl, options);
        return androidDriver;
    }

    @Override
    public void setDriver(AndroidDriver driver) {
        this.androidDriver = driver;
    }

    @Override
    public void closeDriver() {
        if (androidDriver != null) {
            androidDriver.close();
            androidDriver = null;
        }
    }

    @Override
    public void quitDriver() {
        if (androidDriver != null) {
            androidDriver.quit();
            androidDriver = null;
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (androidDriver == null) {
            throw new IllegalStateException("AndroidDriver not initialized.");
        }
    }
}