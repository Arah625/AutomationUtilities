package org.autoutils.driver.safari.manager;


import org.autoutils.driver.DriverManager;
import org.autoutils.driver.safari.factory.SafariDriverFactory;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class SafariDriverManager implements DriverManager<SafariDriver> {

    private final SafariDriverFactory safariDriverFactory;
    private final SafariOptions safariOptions;

    private SafariDriver safariDriver;

    public SafariDriverManager(SafariDriverFactory safariDriverFactory,
                               SafariOptions safariOptions) {
        this.safariDriverFactory = safariDriverFactory;
        this.safariOptions = safariOptions;
    }

    @Override
    public SafariDriver start() {
        if (safariDriver == null) {
            safariDriver = safariDriverFactory.create(safariOptions, null);
        }
        return safariDriver;
    }

    @Override
    public SafariDriver get() {
        if (safariDriver == null) {
            throw new IllegalStateException("ChromeDriver not initialized. Call start() first.");
        }
        return safariDriver;
    }

    @Override
    public void set(SafariDriver driver) {
        this.safariDriver = driver;
    }

    @Override
    public void quit() {
        if (safariDriver != null) {
            try {
                safariDriver.quit();
            } finally {
                safariDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return safariDriverFactory != null && safariDriver.getSessionId() != null;
    }
}