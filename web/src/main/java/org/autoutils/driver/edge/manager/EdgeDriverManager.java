package org.autoutils.driver.edge.manager;


import org.autoutils.driver.DriverManager;
import org.autoutils.driver.edge.factory.EdgeDriverFactory;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverManager implements DriverManager<EdgeDriver> {

    private final EdgeDriverFactory firefoxDriverFactory;
    private final EdgeOptions edgeOptions;

    private EdgeDriver edgeDriver;

    public EdgeDriverManager(EdgeDriverFactory firefoxDriverFactory,
                             EdgeOptions edgeOptions) {
        this.firefoxDriverFactory = firefoxDriverFactory;
        this.edgeOptions = edgeOptions;
    }

    @Override
    public EdgeDriver start() {
        if (edgeDriver == null) {
            edgeDriver = firefoxDriverFactory.create(edgeOptions, null);
        }
        return edgeDriver;
    }

    @Override
    public EdgeDriver get() {
        if (edgeDriver == null) {
            throw new IllegalStateException("ChromeDriver not initialized. Call start() first.");
        }
        return edgeDriver;
    }

    @Override
    public void set(EdgeDriver driver) {
        this.edgeDriver = driver;
    }

    @Override
    public void quit() {
        if (edgeDriver != null) {
            try {
                edgeDriver.quit();
            } finally {
                edgeDriver = null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return firefoxDriverFactory != null && edgeDriver.getSessionId() != null;
    }
}