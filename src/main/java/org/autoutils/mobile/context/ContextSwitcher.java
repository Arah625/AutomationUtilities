package org.autoutils.mobile.context;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.autoutils.mobile.context.exception.ContextNotFoundException;
import org.autoutils.mobile.context.exception.ContextSwitchingUnsupportedException;
import org.autoutils.wait.WaitForCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility class to handle context switching for mobile and web views using Appium.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * public class BasePage {
 *     private final ContextSwitcher contextSwitcher;
 *
 *     public BasePage(AppiumDriver driver) {
 *         this.contextSwitcher = new ContextSwitcher(driver);
 *     }
 * }
 * }
 * </pre>
 */
public class ContextSwitcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextSwitcher.class);

    private final AppiumDriver driver;

    /**
     * Constructs a new ContextSwitcher with the given AppiumDriver.
     *
     * @param driver The AppiumDriver to use for context switching.
     */
    public ContextSwitcher(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Switches to the specified context if it exists.
     * Throws a ContextNotFoundException if the context is not found.
     *
     * @param contextName The name of the context to switch to.
     * @throws ContextNotFoundException             If the specified context is not found.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public void switchToContext(String contextName) {
        LOGGER.debug("Attempting to switch to context: {}", contextName);
        findAndSwitchToContext(() -> findContextByName(contextName));
    }

    /**
     * Waits for the specified context to become available and switches to it.
     *
     * @param contextName     The name of the context to switch to.
     * @param timeout         The maximum time to wait for the context.
     * @param pollingInterval The interval to check for the context.
     * @throws ContextNotFoundException             If the specified context is not found within the timeout.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public void switchToContext(String contextName, Duration timeout, Duration pollingInterval) {
        LOGGER.debug("Waiting for context: {}", contextName);
        waitForAndSwitchToContext(() -> findContextByName(contextName), timeout, pollingInterval);
    }

    /**
     * Attempts to switch to the first available context from the provided context names.
     * If none of the provided contexts are found, throws a ContextNotFoundException and logs all available contexts.
     *
     * @param contextNames A varargs array of context names to switch to.
     * @throws ContextNotFoundException             If none of the provided contexts are found.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public void switchToFirstAvailableContext(String... contextNames) {
        LOGGER.debug("Attempting to switch to first available context from: {}", Arrays.toString(contextNames));
        findAndSwitchToContext(() -> findFirstAvailableContext(Arrays.stream(contextNames)));
    }

    /**
     * Waits for the first available context from the provided list of context names and switches to it.
     *
     * @param timeout         The maximum time to wait for the context.
     * @param pollingInterval The interval to check for the contexts.
     * @param contextNames    A varargs array of context names to switch to.
     * @throws ContextNotFoundException             If none of the contexts are found within the timeout.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public void switchToFirstAvailableContext(Duration timeout, Duration pollingInterval, String... contextNames) {
        LOGGER.debug("Waiting for first available context from: {}", Arrays.toString(contextNames));
        waitForAndSwitchToContext(() -> findFirstAvailableContext(Arrays.stream(contextNames)), timeout, pollingInterval);
    }

    /**
     * Attempts to switch to the first available context from the provided list of context names.
     * If none of the provided contexts are found, throws a ContextNotFoundException and logs all available contexts.
     *
     * @param contextNames A list of context names to switch to.
     * @throws ContextNotFoundException             If none of the provided contexts are found.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public void switchToFirstAvailableContext(List<String> contextNames) {
        LOGGER.debug("Attempting to switch to first available context from list: {}", contextNames);
        findAndSwitchToContext(() -> findFirstAvailableContext(contextNames.stream()));
    }

    /**
     * Waits for the first available context from the provided list of context names and switches to it.
     *
     * @param timeout         The maximum time to wait for the context.
     * @param pollingInterval The interval to check for the contexts.
     * @param contextNames    A list of context names to switch to.
     * @throws ContextNotFoundException             If none of the contexts are found within the timeout.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public void switchToFirstAvailableContext(Duration timeout, Duration pollingInterval, List<String> contextNames) {
        LOGGER.debug("Waiting for first available context from list: {}", contextNames);
        waitForAndSwitchToContext(() -> findFirstAvailableContext(contextNames.stream()), timeout, pollingInterval);
    }

    /**
     * Retrieves the set of available contexts from the driver.
     *
     * @return A set of available context names.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public Set<String> getAvailableContexts() {
        if (driver instanceof SupportsContextSwitching contextSwitchingDriver) {
            Set<String> contexts = contextSwitchingDriver.getContextHandles();
            LOGGER.debug("Available contexts: {}", contexts);
            return contexts;
        } else {
            LOGGER.error("Context listing unsupported on this driver");
            throw new ContextSwitchingUnsupportedException("Context listing is only supported for drivers that implement SupportsContextSwitching.");
        }
    }

    /**
     * Retrieves the current context from the driver.
     *
     * @return The name of the current context.
     * @throws ContextSwitchingUnsupportedException If the driver does not support context switching.
     */
    public String getCurrentContext() {
        if (driver instanceof SupportsContextSwitching contextSwitchingDriver) {
            String currentContext = contextSwitchingDriver.getContext();
            LOGGER.debug("Current context: {}", currentContext);
            return currentContext;
        } else {
            LOGGER.error("Context retrieval unsupported on this driver");
            throw new ContextSwitchingUnsupportedException("Context retrieval is only supported for drivers that implement SupportsContextSwitching.");
        }
    }

    /**
     * Checks if a context with the specified type exists among the available contexts.
     *
     * @param contextType The type of context to check for.
     * @return True if a context with the specified type exists, false otherwise.
     */
    public boolean isContextPresent(String contextType) {
        LOGGER.debug("Checking if context exists with type: {}", contextType);
        return driver instanceof SupportsContextSwitching contextSwitchingDriver &&
                contextSwitchingDriver.getContextHandles()
                                      .stream()
                                      .anyMatch(context -> context.contains(contextType));
    }

    /**
     * Waits for a context with the specified type to become available.
     *
     * @param contextType      The type of context to check for.
     * @param timeout          The maximum time to wait for the context.
     * @param pollingInterval  The interval to check for the context.
     * @return True if a context with the specified type exists, false otherwise.
     */
    public boolean isContextPresent(String contextType, Duration timeout, Duration pollingInterval) {
        LOGGER.debug("Waiting to see if context exists with type: {}", contextType);

        WaitForCondition<Boolean> waitFor = new WaitForCondition<Boolean>(null)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval);

        return waitFor.until(ignored -> isContextPresent(contextType));
    }

    /**
     * Finds a context by its name and returns it.
     * Throws a ContextNotFoundException if the context is not found.
     *
     * @param contextName The name of the context to find.
     * @return The found context name.
     * @throws ContextNotFoundException If the context is not found.
     */
    private String findContextByName(String contextName) {
        return getAvailableContexts()
                .stream()
                .filter(context -> context.contains(contextName))
                .findFirst()
                .orElseThrow(() -> new ContextNotFoundException("No context found with name: " + contextName));
    }

    /**
     * Finds the first available context from a stream of context names.
     * Throws a ContextNotFoundException if none of the provided contexts are found.
     *
     * @param contextNamesStream A stream of context names to search for.
     * @return The found context name.
     * @throws ContextNotFoundException If none of the provided contexts are found.
     */
    private String findFirstAvailableContext(Stream<String> contextNamesStream) {
        return contextNamesStream
                .flatMap(contextName -> getAvailableContexts().stream()
                                                              .filter(context -> context.contains(contextName)))
                .findFirst()
                .orElseThrow(() -> new ContextNotFoundException("None of the provided contexts were found."));
    }

    /**
     * Finds a context using the provided supplier and switches to it.
     *
     * @param contextSupplier The supplier to find the context.
     */
    private void findAndSwitchToContext(Supplier<String> contextSupplier) {
        String context = contextSupplier.get();
        LOGGER.info("Switching to context: {}", context);
        ((SupportsContextSwitching) driver).context(context);
    }

    /**
     * Waits for a context using the provided supplier, and switches to it.
     *
     * @param contextSupplier The supplier to find the context.
     * @param timeout         The maximum time to wait for the context.
     * @param pollingInterval The interval to check for the context.
     */
    private void waitForAndSwitchToContext(Supplier<String> contextSupplier, Duration timeout, Duration pollingInterval) {
        WaitForCondition<String> waitFor = new WaitForCondition<String>(null)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval);

        String context = waitFor.until(ignored -> {
            try {
                return contextSupplier.get();
            } catch (ContextNotFoundException e) {
                return null;
            }
        });

        LOGGER.info("Switching to context: {}", context);
        ((SupportsContextSwitching) driver).context(context);
    }
}
