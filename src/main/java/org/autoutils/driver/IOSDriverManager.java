package org.autoutils.driver;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URL;

public class IOSDriverManager implements Driver<IOSDriver> {

    private IOSDriver iosDriver;
    private static final IOSDriverManager INSTANCE = new IOSDriverManager();

    private IOSDriverManager() {
        // Private constructor to prevent instantiation
    }

    public static IOSDriverManager getInstance() {
        return INSTANCE;
    }

    /**
     * iOS-specific method to initialize the driver with XCUITestOptions.
     *
     * @param options The XCUITestOptions for iOS.
     * @param appiumServerUrl The Appium server URL.
     * @return The initialized IOSDriver instance.
     */
    public IOSDriver getDriver(XCUITestOptions options, URL appiumServerUrl) {
        iosDriver = new IOSDriver(appiumServerUrl, options);
        return iosDriver;
    }

    @Override
    public void setDriver(IOSDriver driver) {
        this.iosDriver = driver;
    }

    @Override
    public void closeDriver() {
        if (iosDriver != null) {
            iosDriver.close();
            iosDriver = null;
        }
    }

    @Override
    public void quitDriver() {
        if (iosDriver != null) {
            iosDriver.quit();
            iosDriver = null;
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (iosDriver == null) {
            throw new IllegalStateException("IOSDriver not initialized.");
        }
    }
}