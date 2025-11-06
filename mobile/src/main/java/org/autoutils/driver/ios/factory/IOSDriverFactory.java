package org.autoutils.driver.ios.factory;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.autoutils.device.DeviceInfo;
import org.autoutils.driver.DriverFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class IOSDriverFactory implements DriverFactory<IOSDriver, XCUITestOptions> {

    @Override
    public IOSDriver create(XCUITestOptions options, URL serverUrl) {
        return new IOSDriver(serverUrl, options);
    }

    public IOSDriver createForDevice(DeviceInfo device, URL serverUrl) {
        XCUITestOptions options = new XCUITestOptions()
                .setUdid(device.udid())
                .setDeviceName(device.name())
                .setPlatformName("iOS")
                .setNoReset(true);
        return create(options, serverUrl);
    }

    public static URL resolveUrl() {
        try {
            return new URL(System.getProperty("appium.url", "http://127.0.0.1:4723"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }
}