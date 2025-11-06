package org.autoutils.device;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe pool for managing mobile devices across parallel test threads.
 */
public class DevicePool {

    private static final List<DeviceInfo> devices = new CopyOnWriteArrayList<>();
    private static final List<String> inUse = new CopyOnWriteArrayList<>();
    private static final ReentrantLock lock = new ReentrantLock();

    public static void addAll(List<DeviceInfo> discoveredDevices) {
        devices.clear();
        devices.addAll(discoveredDevices);
    }

    public static DeviceInfo acquireFreeDevice() {
        lock.lock();
        try {
            for (DeviceInfo device : devices) {
                if (!inUse.contains(device.udid())) {
                    inUse.add(device.udid());
                    return device;
                }
            }
            throw new RuntimeException("No free devices available for test execution.");
        } finally {
            lock.unlock();
        }
    }

    public static void releaseDevice(DeviceInfo device) {
        inUse.remove(device.udid());
    }

    public static List<DeviceInfo> getAllDevices() {
        return List.copyOf(devices);
    }
}