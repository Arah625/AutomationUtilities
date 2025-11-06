package org.autoutils.driver.ios.factory;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.autoutils.driver.DriverFactory;

import java.net.URL;

public class IOSDriverFactory implements DriverFactory<IOSDriver, XCUITestOptions> {

    @Override
    public IOSDriver create(XCUITestOptions options, URL appiumServerUrl) {
        return new IOSDriver(appiumServerUrl, options);
    }
}
