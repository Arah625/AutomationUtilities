package org.autoutils.driver.firefox.factory;

import org.autoutils.driver.DriverFactory;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.URL;

public class FirefoxDriverFactory implements DriverFactory<FirefoxDriver, FirefoxOptions> {

    @Override
    public FirefoxDriver create(FirefoxOptions options, URL serverUrl) {
        return new FirefoxDriver(options);
    }
}
