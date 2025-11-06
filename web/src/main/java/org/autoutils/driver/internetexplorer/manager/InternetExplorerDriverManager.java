package org.autoutils.driver.internetexplorer.manager;


import org.autoutils.driver.DriverManager;
import org.autoutils.driver.internetexplorer.factory.InternetExplorerDriverFactory;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

public class InternetExplorerDriverManager implements DriverManager<InternetExplorerDriver> {

    private final InternetExplorerDriverFactory internetExplorerDriverFactory;
    private final InternetExplorerOptions internetExplorerOptions;

    private InternetExplorerDriver internetExplorerDriver;

    public InternetExplorerDriverManager(InternetExplorerDriverFactory internetExplorerDriverFactory,
                                         InternetExplorerOptions internetExplorerOptions) {
        this.internetExplorerDriverFactory = internetExplorerDriverFactory;
        this.internetExplorerOptions = internetExplorerOptions;
    }

    @Override
    public InternetExplorerDriver start() {
        if (internetExplorerDriver == null) {
            internetExplorerDriver = internetExplorerDriverFactory.create(internetExplorerOptions, null);
        }
        return internetExplorerDriver;
    }

    @Override
    public InternetExplorerDriver get() {
        if (internetExplorerDriver == null) {
            throw new IllegalStateException("ChromeDriver not initialized. Call start() first.");
        }
        return internetExplorerDriver;
    }

    @Override
    public void set(InternetExplorerDriver driver) {
        this.internetExplorerDriver = driver;
    }

    @Override
    public void quit() {
        if (internetExplorerDriver != null) {
            try {
                internetExplorerDriver.quit();
            } finally {
                internetExplorerDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return internetExplorerDriverFactory != null && internetExplorerDriver.getSessionId() != null;
    }
}