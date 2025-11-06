package org.autoutils.driver.firefox.manager;


import org.autoutils.driver.DriverManager;

import org.autoutils.driver.firefox.factory.FirefoxDriverFactory;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager implements DriverManager<FirefoxDriver> {

    private final FirefoxDriverFactory internetExplorerDriverFactory;
    private final FirefoxOptions firefoxOptions;

    private FirefoxDriver firefoxDriver;

    public FirefoxDriverManager(FirefoxDriverFactory internetExplorerDriverFactory,
                                FirefoxOptions firefoxOptions) {
        this.internetExplorerDriverFactory = internetExplorerDriverFactory;
        this.firefoxOptions = firefoxOptions;
    }

    @Override
    public FirefoxDriver start() {
        if (firefoxDriver == null) {
            firefoxDriver = internetExplorerDriverFactory.create(firefoxOptions, null);
        }
        return firefoxDriver;
    }

    @Override
    public FirefoxDriver get() {
        if (firefoxDriver == null) {
            throw new IllegalStateException("ChromeDriver not initialized. Call start() first.");
        }
        return firefoxDriver;
    }

    @Override
    public void set(FirefoxDriver driver) {
        this.firefoxDriver = driver;
    }

    @Override
    public void quit() {
        if (firefoxDriver != null) {
            try {
                firefoxDriver.quit();
            } finally {
                firefoxDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return internetExplorerDriverFactory != null && firefoxDriver.getSessionId() != null;
    }
}