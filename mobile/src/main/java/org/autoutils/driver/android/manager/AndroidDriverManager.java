package org.autoutils.driver.android.manager;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.autoutils.driver.DriverManager;
import org.autoutils.driver.android.factory.AndroidDriverFactory;

import java.net.URL;

public class AndroidDriverManager implements DriverManager<AndroidDriver> {

    private final AndroidDriverFactory androidDriverFactory;
    private final UiAutomator2Options uiAutomator2Options;
    private final URL appiumServerUrl;

    private AndroidDriver androidDriver;

    public AndroidDriverManager(AndroidDriverFactory androidDriverFactory,
                                UiAutomator2Options uiAutomator2Options,
                                URL appiumServerUrl) {
        this.androidDriverFactory = androidDriverFactory;
        this.uiAutomator2Options = uiAutomator2Options;
        this.appiumServerUrl = appiumServerUrl;
    }

    @Override
    public AndroidDriver start() {
        if (androidDriver == null) {
            androidDriver = androidDriverFactory.create(uiAutomator2Options, appiumServerUrl);
        }
        return androidDriver;
    }

    @Override
    public AndroidDriver get() {
        if (androidDriver == null) {
            throw new IllegalStateException("AndroidDriver not initialized. Call start() first.");
        }
        return androidDriver;
    }

    @Override
    public void set(AndroidDriver driver) {
        this.androidDriver = driver;
    }

    @Override
    public void quit() {
        if (androidDriver != null) {
            try {
                androidDriver.quit();
            } finally {
                androidDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return androidDriver != null && androidDriver.getSessionId() != null;
    }
}
