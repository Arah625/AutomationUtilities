package org.autoutils.driver.safari.factory;

import org.autoutils.driver.DriverFactory;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.URL;

public class SafariDriverFactory implements DriverFactory<SafariDriver, SafariOptions> {

    @Override
    public SafariDriver create(SafariOptions options, URL serverUrl) {
        return new SafariDriver(options);
    }
}
