package org.autoutils.mobile.appiumserver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.autoutils.mobile.appiumserver.exception.NoAvailablePortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;

public class AppiumServerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumServerManager.class);

    private AppiumDriverLocalService appiumDriverLocalService;
    private final AppiumServiceBuilder appiumServiceBuilder;
    private final LogManager logManager; // Refactored: Use LogManager for log file management
    private URL serverUrl;

    public AppiumServerManager() {
        this.appiumServiceBuilder = new AppiumServiceBuilder();
        this.logManager = new LogManager(); // Initialize LogManager
    }

    /**
     * Start the Appium server with default settings, using any free port.
     */
    public void startServer() {
        String appiumServerIpAddress = "127.0.0.1";
        applyDefaultArguments(appiumServiceBuilder);
        appiumServiceBuilder
                .withIPAddress(appiumServerIpAddress)
                .usingAnyFreePort();
        startService(appiumServerIpAddress);
        LOGGER.info("Appium server started.");
    }

    /**
     * Start the Appium server with the specified IP address, additional arguments, and optionally a specific port.
     *
     * @param appiumServerIpAddress the IP address to bind the Appium server.
     * @param additionalArgs        a list of additional arguments to pass to the Appium server.
     * @param port                  the port to bind the Appium server (can be null for any free port).
     */
    public void startServer(String appiumServerIpAddress, List<String[]> additionalArgs, int port) {
        appiumServiceBuilder.withIPAddress(appiumServerIpAddress);
        appiumServiceBuilder.usingPort(port);
        applyArguments(appiumServiceBuilder, additionalArgs);
        startService(appiumServerIpAddress);
        LOGGER.info("Appium server started.");
    }

    /**
     * Stop the Appium server if it is running.
     */
    public void stopServer() {
        if (isServerRunning()) {
            appiumDriverLocalService.stop();
            LOGGER.info("Appium server stopped.");
        }
    }

    /**
     * Check if the Appium server is running.
     *
     * @return true if the server is running, false otherwise.
     */
    public boolean isServerRunning() {
        return appiumDriverLocalService != null && appiumDriverLocalService.isRunning();
    }

    /**
     * Finds an available port within the specified range.
     *
     * @param minPort the minimum port number in the range.
     * @param maxPort the maximum port number in the range.
     * @return an available port number within the range.
     * @throws NoAvailablePortException if no available ports are found within the range.
     */
    public int findAvailablePort(int minPort, int maxPort) {
        for (int port = minPort; port <= maxPort; port++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        throw new NoAvailablePortException("No available ports found in the range " + minPort + "-" + maxPort);
    }

    /**
     * Get the URL of the running Appium server.
     *
     * @return the URL of the Appium server.
     */
    public URL getAppiumServerUrl() {
        return this.serverUrl;
    }

    /**
     * Check if a port is available.
     *
     * @param port the port to check.
     * @return true if the port is available, false otherwise.
     */
    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Start the Appium server with the configured AppiumServiceBuilder.
     *
     * @param appiumServerIpAddress the IP address to bind the Appium server.
     */
    private void startService(String appiumServerIpAddress) {
        appiumDriverLocalService = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        appiumDriverLocalService.clearOutPutStreams();
        try {
            // Refactored: Use LogManager to create log files
            appiumDriverLocalService.addOutPutStream(new FileOutputStream(logManager.createLogFile(appiumServerIpAddress, appiumDriverLocalService.getUrl()
                                                                                                                                                  .getPort())));
            appiumDriverLocalService.start();
            serverUrl = appiumDriverLocalService.getUrl();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start Appium server or create log file", e);
        }
    }

    /**
     * Apply a list of arguments to the AppiumServiceBuilder.
     *
     * @param builder        the AppiumServiceBuilder to apply arguments to.
     * @param additionalArgs a list of additional arguments to apply.
     */
    private void applyArguments(AppiumServiceBuilder builder, List<String[]> additionalArgs) {
        for (String[] arg : additionalArgs) {
            if (arg.length == 1) {
                builder.withArgument(() -> arg[0]);
            } else if (arg.length == 2) {
                builder.withArgument(() -> arg[0], arg[1]);
            }
        }
    }

    /**
     * Apply default arguments to the AppiumServiceBuilder.
     *
     * @param builder the AppiumServiceBuilder to apply default arguments to.
     */
    private void applyDefaultArguments(AppiumServiceBuilder builder) {
        List<String[]> defaultArgs = getDefaultArguments();
        applyArguments(builder, defaultArgs);
        builder.withArgument(GeneralServerFlag.LOG_NO_COLORS);
    }

    /**
     * Get default arguments for the Appium server.
     *
     * @return a list of default arguments.
     */
    private List<String[]> getDefaultArguments() {
        return List.of(
                new String[]{GeneralServerFlag.ALLOW_INSECURE.getArgument(), "chromedriver_autodownload"},
                new String[]{GeneralServerFlag.LOG_TIMESTAMP.getArgument()},
                new String[]{GeneralServerFlag.LOG_LEVEL.getArgument(), "debug"},
                new String[]{GeneralServerFlag.LOCAL_TIMEZONE.getArgument()}
        );
    }
}