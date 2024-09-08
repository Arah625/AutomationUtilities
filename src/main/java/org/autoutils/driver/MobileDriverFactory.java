package org.autoutils.driver;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.autoutils.driver.exception.InvalidMobilePlatformException;
import org.autoutils.driver.exception.InvalidOptionsException;
import org.openqa.selenium.WebDriver;

import java.net.URL;


public class MobileDriverFactory {

    /**
     * Get the MobileDriver based on platform and options (Android or iOS).
     *
     * @param platform the platform string (android, ios).
     * @param options the desired capabilities/options for the platform.
     * @return the initialized AppiumDriver (AndroidDriver or IOSDriver).
     */
    public static WebDriver getMobileDriver(String platform, Object options, URL appiumServerUrl) {
        WebDriver driver;

        switch (platform.toLowerCase()) {
            case "android":
                if (options instanceof UiAutomator2Options uiautomator2options) {
                    driver = AndroidDriverManager.getInstance().getDriver(uiautomator2options, appiumServerUrl);
                } else {
                    throw new InvalidOptionsException("Invalid options passed for Android. Expected UiAutomator2Options.");
                }
                break;

            case "ios":
                if (options instanceof XCUITestOptions xcuitestOptions) {
                    driver = IOSDriverManager.getInstance().getDriver(xcuitestOptions, appiumServerUrl);
                } else {
                    throw new InvalidOptionsException("Invalid options passed for iOS. Expected XCUITestOptions.");
                }
                break;

            default:
                throw new InvalidMobilePlatformException("Unsupported mobile platform: " + platform);
        }

        // Register the driver for session management
        DriverSessionManager.registerDriver(driver);
        return driver;
    }
}