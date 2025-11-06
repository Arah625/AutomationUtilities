package org.autoutils.driver.chrome.factory;

import org.autoutils.driver.DriverFactory;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URL;

public class ChromeDriverFactory implements DriverFactory<ChromeDriver, ChromeOptions> {

    @Override
    public ChromeDriver create(ChromeOptions options, URL serverUrl) {
        return new ChromeDriver(options);
    }
}
