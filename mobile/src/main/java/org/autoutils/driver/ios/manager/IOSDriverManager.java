package org.autoutils.driver.ios.manager;

import io.appium.java_client.ios.IOSDriver;
import org.autoutils.device.DeviceInfo;
import org.autoutils.device.DevicePool;
import org.autoutils.driver.DriverManager;
import org.autoutils.driver.ios.factory.IOSDriverFactory;

public class IOSDriverManager implements DriverManager<IOSDriver> {

    private final ThreadLocal<IOSDriver> driverThread = new ThreadLocal<>();
    private final ThreadLocal<DeviceInfo> deviceThread = new ThreadLocal<>();

    @Override
    public IOSDriver start() {
        if (driverThread.get() == null) {
            DeviceInfo device = DevicePool.acquireFreeDevice();
            IOSDriver driver = new IOSDriverFactory().createForDevice(device, IOSDriverFactory.resolveUrl());
            driverThread.set(driver);
            deviceThread.set(device);
        }
        return driverThread.get();
    }

    @Override
    public IOSDriver get() {
        return driverThread.get();
    }

    @Override
    public void set(IOSDriver driver) {
        driverThread.set(driver);
    }

    @Override
    public void quit() {
        IOSDriver driver = driverThread.get();
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