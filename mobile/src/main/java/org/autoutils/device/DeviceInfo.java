package org.autoutils.device;

/**
 * Holds information about a connected mobile device (Android or iOS).
 */
public record DeviceInfo(
        String udid,
        String name,
        String platform, // Android or iOS
        boolean isEmulator
) {
    public boolean isAndroid() {
        return "Android".equalsIgnoreCase(platform);
    }

    public boolean isIOS() {
        return "iOS".equalsIgnoreCase(platform);
    }
}