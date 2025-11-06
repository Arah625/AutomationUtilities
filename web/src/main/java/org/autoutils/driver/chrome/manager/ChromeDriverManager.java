package org.autoutils.driver.chrome.manager;


import org.autoutils.driver.DriverManager;
import org.autoutils.driver.chrome.factory.ChromeDriverFactory;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager implements DriverManager<ChromeDriver> {

    private final ChromeDriverFactory edgeDriverFactory;
    private final ChromeOptions chromeOptions;

    private ChromeDriver chromeDriver;

    public ChromeDriverManager(ChromeDriverFactory edgeDriverFactory,
                               ChromeOptions chromeOptions) {
        this.edgeDriverFactory = edgeDriverFactory;
        this.chromeOptions = chromeOptions;
    }

    @Override
    public ChromeDriver start() {
        if (chromeDriver == null) {
            chromeDriver = edgeDriverFactory.create(chromeOptions, null);
        }
        return chromeDriver;
    }

    @Override
    public ChromeDriver get() {
        if (chromeDriver == null) {
            throw new IllegalStateException("ChromeDriver not initialized. Call start() first.");
        }
        return chromeDriver;
    }

    @Override
    public void set(ChromeDriver driver) {
        this.chromeDriver = driver;
    }

    @Override
    public void quit() {
        if (chromeDriver != null) {
            try {
                chromeDriver.quit();
            } finally {
                chromeDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return edgeDriverFactory != null && chromeDriver.getSessionId() != null;
    }
}