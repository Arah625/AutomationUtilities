package org.autoutils.driver.android.factory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.autoutils.device.DeviceInfo;
import org.autoutils.driver.DriverFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class AndroidDriverFactory implements DriverFactory<AndroidDriver, UiAutomator2Options> {

        @Override
        public AndroidDriver create(UiAutomator2Options options, URL serverUrl) {
            return new AndroidDriver(serverUrl, options);
        }

        public AndroidDriver createForDevice(DeviceInfo device, URL serverUrl) {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setUdid(device.udid())
                    .setDeviceName(device.name())
                    .setPlatformName("Android")
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
