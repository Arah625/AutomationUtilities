package org.autoutils.driver;

public interface Driver<T> {

    /**
     * Sets the driver instance.
     *
     * @param driver The driver instance to be set.
     */
    void setDriver(T driver);

    /**
     * Closes the current driver session.
     */
    void closeDriver();

    /**
     * Quits the driver, effectively ending the session.
     */
    void quitDriver();

    /**
     * Ensures that the driver has been initialized before any operations.
     */
    void ensureDriverInitialized();
}
