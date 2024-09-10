package org.autoutils.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

class AndroidDriverFactory implements Driver<AndroidDriver> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidDriverFactory.class);

    private AndroidDriver androidDriver;
    private static final AndroidDriverFactory INSTANCE = new AndroidDriverFactory();

    private AndroidDriverFactory() {
        // Private constructor to prevent instantiation
    }

    public static AndroidDriverFactory getInstance() {
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
        // Prevent re-initializing the driver if already initialized
        if (androidDriver == null) {
            androidDriver = new AndroidDriver(appiumServerUrl, options);
        }
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
            LOGGER.debug("AndroidDriver session closed.");
        }
    }

    @Override
    public void quitDriver() {
        if (androidDriver != null) {
            androidDriver.quit();
            androidDriver = null;
            LOGGER.debug("AndroidDriver session quit.");
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (androidDriver == null) {
            throw new IllegalStateException("AndroidDriver not initialized.");
        }
    }
}
