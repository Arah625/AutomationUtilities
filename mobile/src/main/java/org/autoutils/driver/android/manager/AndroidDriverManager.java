package org.autoutils.driver.android.manager;

import io.appium.java_client.android.AndroidDriver;
import org.autoutils.device.DeviceInfo;
import org.autoutils.device.DevicePool;
import org.autoutils.driver.DriverManager;
import org.autoutils.driver.android.factory.AndroidDriverFactory;

public class AndroidDriverManager implements DriverManager<AndroidDriver> {

    private final ThreadLocal<AndroidDriver> driverThread = new ThreadLocal<>();
    private final ThreadLocal<DeviceInfo> deviceThread = new ThreadLocal<>();

    @Override
    public AndroidDriver start() {
        if (driverThread.get() == null) {
            DeviceInfo device = DevicePool.acquireFreeDevice();
            AndroidDriver driver = new AndroidDriverFactory().createForDevice(device, AndroidDriverFactory.resolveUrl());
            driverThread.set(driver);
            deviceThread.set(device);
        }
        return driverThread.get();
    }

    @Override
    public AndroidDriver get() {
        return driverThread.get();
    }

    @Override
    public void set(AndroidDriver driver) {
        driverThread.set(driver);
    }

    @Override
    public void quit() {
        AndroidDriver driver = driverThread.get();
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

    @Override
    public boolean isRunning() {
        return driverThread.get() != null;
    }
}