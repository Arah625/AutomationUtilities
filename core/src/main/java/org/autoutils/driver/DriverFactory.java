package org.autoutils.driver;

import java.net.URL;

public interface DriverFactory<D, O> {

    /**
     * For Web serverUrl == null; For Mobile it is URL Appium.
     */
    D create(O options, URL serverUrl);
}
