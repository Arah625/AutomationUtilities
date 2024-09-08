package org.autoutils.driver;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class DriverSessionManager {

    // List to hold all active driver instances
    private static List<WebDriver> activeDrivers = new ArrayList<>();

    /**
     * Add a driver to the list of active drivers.
     *
     * @param driver The driver instance to be added.
     */
    public static void registerDriver(WebDriver driver) {
        activeDrivers.add(driver);
    }

    /**
     * Quit all active drivers.
     */
    public static void quitAllDrivers() {
        for (WebDriver driver : activeDrivers) {
            if (driver != null) {
                driver.quit();
            }
        }
        // Clear the list after quitting all drivers
        activeDrivers.clear();
    }

    /**
     * Get the number of active drivers.
     *
     * @return the number of currently active drivers.
     */
    public static int getActiveDriverCount() {
        return activeDrivers.size();
    }
}