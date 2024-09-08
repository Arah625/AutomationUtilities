package org.autoutils.driver;

import org.autoutils.driver.exception.MissingConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    // Load properties from the specified path
    public static void loadProperties(String propertiesPath) {
        try {
            FileInputStream configFile = new FileInputStream(propertiesPath);
            properties.load(configFile);
        } catch (IOException e) {
            throw new MissingConfigurationException("Properties file not found at the specified location: " + propertiesPath);
        }
    }

    // Get platform from the properties file
    public static String getPlatform() {
        return properties.getProperty("platform");
    }
}
