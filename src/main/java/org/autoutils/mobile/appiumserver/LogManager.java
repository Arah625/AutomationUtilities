package org.autoutils.mobile.appiumserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    /**
     * Create a log file for the Appium server instance with the given IP address and port.
     *
     * @param ipAddress the IP address to bind the Appium server.
     * @param port      the port to bind the Appium server.
     * @return the File object pointing to the log file.
     */
    public File createLogFile(String ipAddress, int port) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String logFileName = "Appium_" + ipAddress + "_" + port + "_" + timestamp + ".log";
        File logFile = new File("appiumlogs", logFileName);

        // Ensure the logs directory exists
        logFile.getParentFile().mkdirs();

        return logFile;
    }

    /**
     * Create a log file for the Appium server instance with the given IP address.
     *
     * @param ipAddress the IP address to bind the Appium server.
     * @return the File object pointing to the log file.
     */
    public File createLogFile(String ipAddress) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String logFileName = "Appium_" + ipAddress + "_" + timestamp + ".log";
        File logFile = new File("appiumlogs", logFileName);

        // Ensure the logs directory exists
        logFile.getParentFile().mkdirs();

        return logFile;
    }
}