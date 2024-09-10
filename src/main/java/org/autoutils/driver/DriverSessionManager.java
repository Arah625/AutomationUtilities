package org.autoutils.driver;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DriverSessionManager {

    // Thread-safe list to hold all active driver instances
    private static List<WebDriver> activeDrivers = new CopyOnWriteArrayList<>();

    /**
     * Add a driver to the list of active drivers.
     *
     * @param driver The driver instance to be added.
     */
    public static void registerDriver(WebDriver driver) {
        if (driver != null) {
            activeDrivers.add(driver);
        }
    }

    /**
     * Quit all active drivers.
     */
    public static void quitAllDrivers() {
        for (WebDriver driver : activeDrivers) {
            if (driver != null) {
                driver.quit();
                System.out.println("Driver session quit.");
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
