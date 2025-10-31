package org.autoutils.driver;

import org.autoutils.driver.exception.MissingConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    /**
     * Load properties from the specified path.
     *
     * @param propertiesPath The file path to the properties file.
     */
    public static void loadProperties(String propertiesPath) {
        try (FileInputStream configFile = new FileInputStream(propertiesPath)) {
            properties.load(configFile);
        } catch (IOException e) {
            throw new MissingConfigurationException("Properties file not found at the specified location: " + propertiesPath);
        }
    }

    /**
     * Get platform from the properties file.
     *
     * @return The platform name (android, ios, web, etc.), or null if not found.
     */
    public static String getPlatform() {
        return properties.getProperty("platform", "default-platform");  // Optional default value if platform is not set
    }

    /**
     * Generic method to fetch a property by key.
     *
     * @param key The key for the property.
     * @return The value of the property, or null if not found.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
