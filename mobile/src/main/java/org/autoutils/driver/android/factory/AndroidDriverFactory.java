package org.autoutils.driver.android.factory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.autoutils.driver.DriverFactory;

import java.net.URL;

public class AndroidDriverFactory implements DriverFactory<AndroidDriver, UiAutomator2Options> {

    @Override
    public AndroidDriver create(UiAutomator2Options options, URL appiumServerUrl) {
        return new AndroidDriver(appiumServerUrl, options);
    }
}
