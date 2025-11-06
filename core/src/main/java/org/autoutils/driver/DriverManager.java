package org.autoutils.driver;

public interface DriverManager<D> {

    /**
     * Creates and/or starts the driver session if not running, * then returns the active driver instance.
     */
    D start();

    /**
     * Returns the current driver instance, or {@code null} if not started.
     */
    D get();

    void set(D driver);

    /**
     * Quits the driver session if running.
     */
    void quit();

    /**
     * Returns {@code true} if a driver session is currently active.
     */
    boolean isRunning();
}
