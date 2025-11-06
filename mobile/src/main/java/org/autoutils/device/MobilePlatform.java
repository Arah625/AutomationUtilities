package org.autoutils.device;

import org.autoutils.driver.exception.InvalidMobilePlatformException;
import org.autoutils.driver.exception.UnknownPlatformException;

public enum MobilePlatform {
    ANDROID, IOS;


    public static MobilePlatform fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidMobilePlatformException("Platform not provided or empty.");
        }
        try {
            return MobilePlatform.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new UnknownPlatformException("Unsupported platform: " + value);
        }
    }
}