package org.autoutils.driver;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

class IOSDriverFactory implements Driver<IOSDriver> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOSDriverFactory.class);

    private IOSDriver iosDriver;
    private static final IOSDriverFactory INSTANCE = new IOSDriverFactory();

    private IOSDriverFactory() {
        // Private constructor to prevent instantiation
    }

    public static IOSDriverFactory getInstance() {
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
        // Prevent re-initializing the driver if already initialized
        if (iosDriver == null) {
            iosDriver = new IOSDriver(appiumServerUrl, options);
        }
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
            LOGGER.debug("IOSDriver session closed.");
        }
    }

    @Override
    public void quitDriver() {
        if (iosDriver != null) {
            iosDriver.quit();
            iosDriver = null;
            LOGGER.debug("IOSDriver session quit.");
        }
    }

    @Override
    public void ensureDriverInitialized() {
        if (iosDriver == null) {
            throw new IllegalStateException("IOSDriver not initialized.");
        }
    }
}
