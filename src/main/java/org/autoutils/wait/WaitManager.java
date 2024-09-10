package org.autoutils.wait;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

/**
 * WaitManager provides a Singleton utility to manage both WebDriverWait and FluentWait instances for Selenium WebDriver.
 * It allows users to set custom timeouts, polling intervals, and exceptions to ignore while waiting for conditions.
 */
public class WaitManager {

    private static WaitManager instance;  // Singleton instance
    private final WebDriver webDriver;    // WebDriver instance for waits
    private static final Duration DEFAULT_WAIT_TIME = Duration.ofSeconds(10);   // Default timeout for waits
    private static final Duration DEFAULT_POLLING_INTERVAL = Duration.ofMillis(250);  // Default polling interval

    /**
     * Private constructor to prevent direct instantiation.
     *
     * @param webDriver The WebDriver instance to be used for waits.
     */
    private WaitManager(WebDriver webDriver) {
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver must be initialized before using WaitManager.");
        }
        this.webDriver = webDriver;
    }

    /**
     * Provides the Singleton instance of WaitManager.
     *
     * @param webDriver The WebDriver instance to be used.
     * @return The Singleton instance of WaitManager.
     */
    public static WaitManager getInstance(WebDriver webDriver) {
        if (instance == null) {
            instance = new WaitManager(webDriver);
        }
        return instance;
    }

    // ========================= WebDriverWait Methods =========================

    /**
     * Provides a {@link WebDriverWait} instance with the default timeout (10 seconds).
     *
     * @return A {@link WebDriverWait} instance with the default timeout.
     */
    public WebDriverWait getWebDriverWait() {
        return new WebDriverWait(webDriver, DEFAULT_WAIT_TIME);
    }

    /**
     * Provides a {@link WebDriverWait} instance with a custom timeout.
     *
     * @param timeout The custom timeout duration.
     * @return A {@link WebDriverWait} instance with the specified timeout.
     */
    public WebDriverWait getWebDriverWait(Duration timeout) {
        return new WebDriverWait(webDriver, timeout);
    }

    // ========================= FluentWait Methods =========================

    /**
     * Provides a {@link FluentWait} instance with a custom timeout, polling interval, and ignored exceptions.
     *
     * @param timeout            The maximum time to wait for a condition to become true.
     * @param pollingInterval    The frequency with which to check the condition.
     * @param exceptionsToIgnore The exceptions to ignore while polling for a condition.
     * @return A {@link FluentWait} instance configured with the specified options.
     */
    @SafeVarargs
    public final FluentWait<WebDriver> getFluentWait(Duration timeout, Duration pollingInterval, Class<? extends Throwable>... exceptionsToIgnore) {
        return new FluentWait<>(webDriver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoreAll(Arrays.asList(exceptionsToIgnore));
    }

    /**
     * Provides a {@link FluentWait} instance with a custom timeout and polling interval.
     * This method uses {@link NoSuchElementException} as the default exception to ignore.
     *
     * @param timeout         The maximum time to wait for a condition to become true.
     * @param pollingInterval The frequency with which to check the condition.
     * @return A {@link FluentWait} instance configured with the specified timeout and polling interval.
     */
    public final FluentWait<WebDriver> getFluentWait(Duration timeout, Duration pollingInterval) {
        return getFluentWait(timeout, pollingInterval, NoSuchElementException.class);
    }

    /**
     * Provides a {@link FluentWait} instance using the default timeout (10 seconds) and polling interval (250 milliseconds).
     * This configuration is useful for common use cases where default values are sufficient.
     *
     * @return A {@link FluentWait} instance with the default configuration.
     */
    public final FluentWait<WebDriver> getFluentWait() {
        return getFluentWait(DEFAULT_WAIT_TIME, DEFAULT_POLLING_INTERVAL, NoSuchElementException.class);
    }

    /**
     * Provides a {@link FluentWait} instance with a customizable polling interval and exceptions to ignore,
     * using the default timeout (10 seconds).
     *
     * @param pollingInterval    The interval at which to poll for the condition.
     * @param exceptionsToIgnore The types of exceptions to ignore while polling for a condition.
     * @return A {@link FluentWait} instance configured with the specified polling interval and exceptions to ignore.
     */
    @SafeVarargs
    public final FluentWait<WebDriver> getFluentWait(Duration pollingInterval, Class<? extends Throwable>... exceptionsToIgnore) {
        return getFluentWait(DEFAULT_WAIT_TIME, pollingInterval, exceptionsToIgnore);
    }

    /**
     * Provides a {@link FluentWait} instance using the default timeout and polling interval (10 seconds and 250 milliseconds),
     * with specific exceptions to ignore during the polling period.
     *
     * @param exceptionsToIgnore The exceptions to ignore while waiting for the condition.
     * @return A {@link FluentWait} instance with the default timeout, polling interval, and specified ignored exceptions.
     */
    @SafeVarargs
    public final FluentWait<WebDriver> getFluentWait(Class<? extends Throwable>... exceptionsToIgnore) {
        return getFluentWait(DEFAULT_WAIT_TIME, DEFAULT_POLLING_INTERVAL, exceptionsToIgnore);
    }

    /**
     * Provides a {@link FluentWait} instance with a customizable polling interval, using the default timeout (10 seconds).
     * This method focuses on adjusting the polling interval while using default timing for the overall wait duration.
     *
     * @param pollingInterval The interval at which to poll for the condition.
     * @return A {@link FluentWait} instance configured with the specified polling interval and default wait time.
     */
    public final FluentWait<WebDriver> getFluentWait(Duration pollingInterval) {
        return getFluentWait(DEFAULT_WAIT_TIME, pollingInterval, NoSuchElementException.class);
    }
}