package org.autoutils.device;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to discover connected Android and iOS devices.
 */
public class DeviceDiscoveryService {

    public static List<DeviceInfo> discoverAndroidDevices() {
        List<DeviceInfo> devices = new ArrayList<>();
        try {
            Process process = new ProcessBuilder("adb", "devices", "-l").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("model:")) {
                    String[] parts = line.split("\\s+");
                    String udid = parts[0];
                    boolean isEmulator = udid.startsWith("emulator");
                    String name = line.contains("model:") ? line.split("model:")[1].split("\\s")[0] : "Android";
                    devices.add(new DeviceInfo(udid, name, "Android", isEmulator));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to discover Android devices", e);
        }
        return devices;
    }

    public static List<DeviceInfo> discoverIOSDevices() {
        List<DeviceInfo> devices = new ArrayList<>();
        String os = System.getProperty("os.name").toLowerCase();

        if (!os.contains("mac")) {
            System.out.println("[INFO] Skipping iOS device discovery: not a macOS system.");
            return devices;
        }

        try {
            Process process = new ProcessBuilder("xcrun", "xctrace", "list", "devices").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("(")) {
                    String name = line.substring(0, line.indexOf("(")).trim();
                    String udid = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                    boolean isEmulator = line.contains("Simulator");
                    devices.add(new DeviceInfo(udid, name, "iOS", isEmulator));
                }
            }
        } catch (Exception e) {
            System.err.println("[WARN] Skipping iOS device discovery due to error: " + e.getMessage());
        }

        return devices;
    }

    public static List<DeviceInfo> discoverAllDevices() {
        List<DeviceInfo> all = new ArrayList<>();
        all.addAll(discoverAndroidDevices());
        all.addAll(discoverIOSDevices());
        return all;
    }
}