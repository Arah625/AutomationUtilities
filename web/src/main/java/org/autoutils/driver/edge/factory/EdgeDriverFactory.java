package org.autoutils.driver.edge.factory;

import org.autoutils.driver.DriverFactory;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.net.URL;

public class EdgeDriverFactory implements DriverFactory<EdgeDriver, EdgeOptions> {

    @Override
    public EdgeDriver create(EdgeOptions options, URL serverUrl) {
        return new EdgeDriver(options);
    }
}
