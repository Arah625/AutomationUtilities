package org.autoutils.driver;

import io.appium.java_client.AppiumDriver;
import org.autoutils.device.DeviceInfo;
import org.autoutils.device.DevicePool;
import org.autoutils.driver.android.factory.AndroidDriverFactory;
import org.autoutils.driver.ios.factory.IOSDriverFactory;

public class ThreadLocalDriverManager {

    private static final ThreadLocal<AppiumDriver> driverThread = new ThreadLocal<>();
    private static final ThreadLocal<DeviceInfo> deviceThread = new ThreadLocal<>();

    public static void createDriver() {
        if (driverThread.get() != null) {
            return;
        }

        DeviceInfo device = DevicePool.acquireFreeDevice();
        AppiumDriver driver;

        if (device.isAndroid()) {
            driver = new AndroidDriverFactory().createForDevice(device, AndroidDriverFactory.resolveUrl());
        } else if (device.isIOS()) {
            driver = new IOSDriverFactory().createForDevice(device, IOSDriverFactory.resolveUrl());
        } else {
            throw new UnsupportedOperationException("Unsupported platform: " + device.platform());
        }

        driverThread.set(driver);
        deviceThread.set(device);
    }

    public static AppiumDriver getDriver() {
        return driverThread.get();
    }

    public static DeviceInfo getDevice() {
        return deviceThread.get();
    }

    public static void quitDriver() {
        AppiumDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }

        DeviceInfo device = deviceThread.get();
        if (device != null) {
            DevicePool.releaseDevice(device);
            deviceThread.remove();
        }
    }

    public static boolean isDriverActive() {
        return driverThread.get() != null;
    }
}