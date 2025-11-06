package org.autoutils.driver.internetexplorer.factory;

import org.autoutils.driver.DriverFactory;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import java.net.URL;

public class InternetExplorerDriverFactory implements DriverFactory<InternetExplorerDriver, InternetExplorerOptions> {

    @Override
    public InternetExplorerDriver create(InternetExplorerOptions options, URL serverUrl) {
        return new InternetExplorerDriver(options);
    }
}
