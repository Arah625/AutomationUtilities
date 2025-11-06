package org.autoutils.driver.ios.manager;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.autoutils.driver.DriverManager;
import org.autoutils.driver.ios.factory.IOSDriverFactory;

import java.net.URL;

public class IOSDriverManager implements DriverManager<IOSDriver> {

    private final IOSDriverFactory iosDriverFactory;
    private final XCUITestOptions xcuiTestOptions;
    private final URL appiumServerUrl;

    private IOSDriver iosDriver;

    public IOSDriverManager(IOSDriverFactory iosDriverFactory,
                            XCUITestOptions xcuiTestOptions,
                            URL appiumServerUrl) {
        this.iosDriverFactory = iosDriverFactory;
        this.xcuiTestOptions = xcuiTestOptions;
        this.appiumServerUrl = appiumServerUrl;
    }

    @Override
    public IOSDriver start() {
        if (iosDriver == null) {
            iosDriver = iosDriverFactory.create(xcuiTestOptions, appiumServerUrl);
        }
        return iosDriver;
    }

    @Override
    public IOSDriver get() {
        if (iosDriver == null) {
            throw new IllegalStateException("IOSDriver not initialized. Call start() first.");
        }
        return iosDriver;
    }

    @Override
    public void set(IOSDriver driver) {
        this.iosDriver = driver;
    }

    @Override
    public void quit() {
        if (iosDriver != null) {
            try {
                iosDriver.quit();
            } finally {
                iosDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return iosDriver != null && iosDriver.getSessionId() != null;
    }
}