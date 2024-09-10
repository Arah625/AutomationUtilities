package org.autoutils.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.autoutils.driver.exception.InvalidMobilePlatformException;
import org.autoutils.driver.exception.UnknownPlatformException;

import java.net.URL;

/**
 * MobileDriverManager is responsible for providing and initializing the appropriate mobile driver
 * (Android or iOS) based on the provided platform or configuration.
 */
public class MobileDriverManager {

    /**
     * Get mobile driver for Android using UiAutomator2Options.
     *
     * @param options         The UiAutomator2Options for Android.
     * @param appiumServerUrl The URL of the Appium server.
     * @return The initialized Android AppiumDriver instance.
     */
    public static AppiumDriver getAndroidDriver(UiAutomator2Options options, URL appiumServerUrl) {
        AppiumDriver driver = AndroidDriverFactory.getInstance().getDriver(options, appiumServerUrl);
        DriverSessionManager.registerDriver(driver);
        System.out.println("Android driver initialized successfully.");
        return driver;
    }

    /**
     * Get mobile driver for iOS using XCUITestOptions.
     *
     * @param options         The XCUITestOptions for iOS.
     * @param appiumServerUrl The URL of the Appium server.
     * @return The initialized iOS AppiumDriver instance.
     */
    public static AppiumDriver getIOSDriver(XCUITestOptions options, URL appiumServerUrl) {
        AppiumDriver driver = IOSDriverFactory.getInstance().getDriver(options, appiumServerUrl);
        DriverSessionManager.registerDriver(driver);
        System.out.println("iOS driver initialized successfully.");
        return driver;
    }

    /**
     * Get mobile driver automatically based on the platform passed by the user.
     * This method switches between Android and iOS based on the platform argument.
     *
     * @param platform        The platform specified by the user ("android" or "ios").
     * @param androidOptions  The UiAutomator2Options for Android.
     * @param iosOptions      The XCUITestOptions for iOS.
     * @param appiumServerUrl The URL of the Appium server.
     * @return The initialized AppiumDriver instance (Android or iOS).
     */
    public static AppiumDriver getMobileDriver(String platform, UiAutomator2Options androidOptions, XCUITestOptions iosOptions, URL appiumServerUrl) {
        validatePlatform(platform);  // Validate platform string

        return switch (platform.toLowerCase()) {
            case "android" -> getAndroidDriver(androidOptions, appiumServerUrl);
            case "ios" -> getIOSDriver(iosOptions, appiumServerUrl);
            default -> throw new UnknownPlatformException("Unsupported platform: " + platform);  // Fallback, shouldn't reach here
        };
    }

    /**
     * Get mobile driver by detecting the platform from the configuration file (as a fallback).
     * This method switches between Android and iOS based on the platform specified in the properties file.
     *
     * @param androidOptions  The UiAutomator2Options for Android.
     * @param iosOptions      The XCUITestOptions for iOS.
     * @param appiumServerUrl The URL of the Appium server.
     * @return The initialized AppiumDriver instance (Android or iOS).
     */
    public static AppiumDriver getMobileDriver(UiAutomator2Options androidOptions, XCUITestOptions iosOptions, URL appiumServerUrl) {
        String platform = ConfigManager.getPlatform();  // Load platform from configuration
        validatePlatform(platform);  // Validate platform string
        return getMobileDriver(platform, androidOptions, iosOptions, appiumServerUrl);
    }

    /**
     * Helper method to validate the platform string.
     * This ensures that the platform is either "android" or "ios" and throws appropriate exceptions for invalid values.
     *
     * @param platform The platform to validate (must be "android" or "ios").
     * @throws InvalidMobilePlatformException if the platform is null or empty.
     * @throws UnknownPlatformException       if the platform is not "android" or "ios".
     */
    private static void validatePlatform(String platform) {
        if (platform == null || platform.trim().isEmpty()) {
            throw new InvalidMobilePlatformException("Platform not provided or empty.");
        }
        if (!platform.equalsIgnoreCase("android") && !platform.equalsIgnoreCase("ios")) {
            throw new UnknownPlatformException("Unsupported platform: " + platform);
        }
    }
}
