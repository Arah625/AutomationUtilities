package org.autoutils.wait;

import org.autoutils.wait.exception.WaitTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * WaitForCondition is a utility class that provides a flexible waiting mechanism.
 * It allows for configurable timeouts, polling intervals, and exception handling.
 * By default, it waits up to 30 seconds, polling every 500 milliseconds.
 *
 * <p><b>Note:</b> This class is not designed to work with `void` methods. It expects
 * a return value to determine when the condition has been met. If you need to wait
 * for a `void` method to complete, consider using a different approach, such as using
 * a boolean flag to indicate completion.</p>
 *
 * <p>Example of initialization in a `BasePage` class:</p>
 * <pre>{@code
 * public abstract class BasePage {
 *     protected WaitForCondition<BasePage> waitForCondition;
 *
 *     public BasePage() {
 *         this.waitForCondition = new WaitForCondition<>(this)
 *             .withTimeout(Duration.ofSeconds(20))
 *             .pollingEvery(Duration.ofMillis(250));
 *     }
 * }
 * }</pre>
 *
 * @param <T> The type of the input used in the waiting condition.
 */
public class WaitForCondition<T> {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30); // Default to 30 seconds
    private static final Duration DEFAULT_POLLING_INTERVAL = Duration.ofMillis(500); // Default to 500 milliseconds
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitForCondition.class);

    private final T input;
    private final Clock clock;
    private Duration timeout = DEFAULT_TIMEOUT;
    private Duration pollingInterval = DEFAULT_POLLING_INTERVAL;
    private final List<Class<? extends Throwable>> ignoredExceptions = new ArrayList<>();

    /**
     * Constructs a WaitForCondition instance with default timeout (30 seconds) and polling interval (500 milliseconds).
     *
     * <p>Example of usage in a method:</p>
     * <pre>{@code
     * public String waitForStringCondition() {
     *     WaitForCondition<String> waitFor = new WaitForCondition<>("Initial Value");
     *     return waitFor.until(value -> value.equals("Expected Value") ? "Success" : null);
     * }
     * }</pre>
     *
     * @param input The input to apply the waiting condition to.
     */
    public WaitForCondition(T input) {
        this(input, Clock.systemDefaultZone());
    }

    /**
     * Constructs a WaitForCondition instance with a specific clock, default timeout (30 seconds),
     * and polling interval (500 milliseconds).
     *
     * @param input The input to apply the waiting condition to.
     * @param clock The clock to use when measuring the timeout.
     */
    public WaitForCondition(T input, Clock clock) {
        this.input = input;
        this.clock = clock;
    }

    /**
     * Sets the timeout duration for the wait operation.
     *
     * <p>Example of usage in a method:</p>
     * <pre>{@code
     * public void configureTimeout() {
     *     WaitForCondition<Integer> waitFor = new WaitForCondition<>(100);
     *     waitFor.withTimeout(Duration.ofSeconds(10));  // Sets timeout to 10 seconds
     * }
     * }</pre>
     *
     * @param timeout The timeout duration.
     * @return A reference to this WaitForCondition instance for method chaining.
     */
    public WaitForCondition<T> withTimeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Sets the polling interval for the wait operation.
     *
     * <p>Example of usage in a method:</p>
     * <pre>{@code
     * public void configurePollingInterval() {
     *     WaitForCondition<Integer> waitFor = new WaitForCondition<>(100);
     *     waitFor.pollingEvery(Duration.ofMillis(100));  // Sets polling interval to 100 milliseconds
     * }
     * }</pre>
     *
     * @param interval The polling interval duration.
     * @return A reference to this WaitForCondition instance for method chaining.
     */
    public WaitForCondition<T> pollingEvery(Duration interval) {
        this.pollingInterval = interval;
        return this;
    }

    /**
     * Configures this WaitForCondition instance to ignore specific types of exceptions while waiting for a condition.
     *
     * <p>Example of usage in a method:</p>
     * <pre>{@code
     * public void configureIgnoredExceptions() {
     *     WaitForCondition<String> waitFor = new WaitForCondition<>("Initial Value");
     *     waitFor.ignoring(IllegalArgumentException.class);  // Ignores IllegalArgumentException during wait
     * }
     * }</pre>
     *
     * @param exceptionType The type of exception to ignore.
     * @return A reference to this WaitForCondition instance for method chaining.
     */
    public WaitForCondition<T> ignoring(Class<? extends Throwable> exceptionType) {
        ignoredExceptions.add(exceptionType);
        return this;
    }

    /**
     * Repeatedly applies this instance's input to the given function until the function returns a non-null,
     * non-false value, or the timeout expires.
     *
     * <p><b>Note:</b> This method does not support `void` methods. It expects a return value to determine when
     * the condition has been met. If using a `void` method, consider using a boolean flag or another mechanism
     * to signal completion.</p>
     *
     * <p>Example of usage with a boolean condition in a method:</p>
     * <pre>{@code
     * public boolean waitForBooleanCondition() {
     *     WaitForCondition<Boolean> waitFor = new WaitForCondition<>(false);
     *     return waitFor.until(value -> value ? Boolean.TRUE : null);
     * }
     * }</pre>
     *
     * <p>Example of usage with a String condition in a method:</p>
     * <pre>{@code
     * public String waitForStringCondition() {
     *     WaitForCondition<String> waitFor = new WaitForCondition<>("Hello");
     *     return waitFor.until(value -> value.contains("World") ? "Success" : null);
     * }
     * }</pre>
     *
     * <p>Example of usage with an Integer condition in a method:</p>
     * <pre>{@code
     * public Integer waitForIntegerCondition() {
     *     WaitForCondition<Integer> waitFor = new WaitForCondition<>(0);
     *     return waitFor.until(value -> value >= 10 ? value : null);
     * }
     * }</pre>
     *
     * @param <V> The function's expected return type.
     * @param condition The condition to evaluate.
     * @return The function's return value if the condition is met within the timeout period.
     * @throws RuntimeException if the timeout expires before the condition is met.
     */
    public <V> V until(Function<? super T, V> condition) {
        Instant start = clock.instant();
        Instant end = start.plus(timeout);
        Throwable lastException;

        while (true) {
            Instant now = clock.instant();
            Duration elapsedTime = Duration.between(start, now);
            Duration remainingTime = Duration.between(now, end).isNegative() ? Duration.ZERO : Duration.between(now, end);

            try {
                V value = condition.apply(input);
                if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                    LOGGER.debug("Condition met with value: {} after {} seconds.", value, elapsedTime.toSeconds());
                    return value;
                }

                lastException = null;
                LOGGER.debug("Condition not met, continuing to wait... Elapsed time: {} seconds, Remaining time: {} seconds.",
                        elapsedTime.toSeconds(), remainingTime.toSeconds());
            } catch (Exception e) { // Catching Exception instead of Throwable
                lastException = propagateIfNotIgnored(e);
                LOGGER.warn("Exception occurred while evaluating condition: {}", e.toString());
            }

            if (remainingTime.isZero()) {
                LOGGER.error("Timeout reached after {} seconds, condition not met.", elapsedTime.toSeconds());
                throw timeoutException("Condition not met within the timeout", lastException);
            }

            try {
                LOGGER.debug("Waiting for {} milliseconds before retrying...", pollingInterval.toMillis());
                Thread.sleep(pollingInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Waiting was interrupted", e);
            }
        }
    }

    private Throwable propagateIfNotIgnored(Throwable e) {
        for (Class<? extends Throwable> ignoredException : ignoredExceptions) {
            if (ignoredException.isInstance(e)) {
                return e;
            }
        }
        if (e instanceof Error error) {
            throw error;
        }
        if (e instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        throw new RuntimeException(e);
    }

    /**
     * Throws a timeout exception with a message and a cause. This method may be overridden to throw an exception
     * that is idiomatic for a particular use case.
     *
     * @param message The timeout message.
     * @param lastException The last exception that occurred during the wait.
     * @return A RuntimeException indicating the timeout.
     */
    protected WaitTimeoutException timeoutException(String message, Throwable lastException) {
        return new WaitTimeoutException(message, lastException);
    }
}
