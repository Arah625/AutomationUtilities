package org.autoutils.action;

import io.appium.java_client.AppiumDriver;
import org.autoutils.action.exception.ElementNotFoundException;
import org.autoutils.action.exception.SwipeExecutionException;
import org.autoutils.action.exception.SwipeInterruptedException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.*;

/**
 * SwipeHandler provides a set of methods to perform swipe gestures on a mobile device.
 * It allows you to swipe in specific directions, either by specifying start and end percentages
 * or by continuously swiping until a specific element is found on the screen.
 *
 * <p>Usage Example 1 (initializing in a screen class constructor):
 * <pre>
 * {@code
 * public class BasicScreen {
 *     protected AppiumDriver appiumDriver;
 *     protected WebDriver driver;
 *     protected WebDriverWait webDriverWait;
 *     protected SwipeHandler swipeHandler;
 *
 *     public BasicScreen(WebDriver driver, WebDriverWait webDriverWait, AppiumDriver appiumDriver) {
 *         this.driver = driver;
 *         this.webDriverWait = webDriverWait;
 *         this.appiumDriver = appiumDriver;
 *         this.swipeHandler = new SwipeHandler(appiumDriver);
 *         PageFactory.initElements(new AppiumFieldDecorator(driver), this);
 *     }
 * }
 * }
 * </pre>
 *
 * <p>Usage Example 2 (direct initialization in a method):
 * <pre>
 * {@code
 * SwipeHandler swipeHandler = new SwipeHandler(appiumDriver);
 * swipeHandler.swipeInDirectionWithPercentage("up", 0.7, 0.3);
 * }
 * </pre>
 */
public class SwipeHandler {

    private static final Logger logger = LoggerFactory.getLogger(SwipeHandler.class);
    private static final double SWIPE_START_RATIO = 0.8;
    private static final double SWIPE_END_RATIO = 0.2;
    private static final Duration DEFAULT_SWIPE_DURATION = Duration.ofMillis(500);

    private final AppiumDriver appiumDriver;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    // Direction constants
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";

    /**
     * Constructor for SwipeHandler.
     * Initializes the handler with the given AppiumDriver instance.
     *
     * @param appiumDriver The AppiumDriver used to perform swipe actions.
     */
    public SwipeHandler(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    /**
     * Swipes up across the screen to reveal hidden content.
     * This method is typically used when you need to scroll up to view more items or information.
     *
     * <p>Usage in a method:
     * <pre>
     * {@code
     * public HomeScreen swipeToTopNews() {
     *     swipeHandler.swipeUp();
     *     WebElement topNewsItem = appiumDriver.findElement(By.id("top_news_item"));
     *     topNewsItem.click();
     *     return this;
     * }
     * }
     * </pre>
     */
    public void swipeUp() {
        swipeInDirectionWithPercentage(UP, SWIPE_START_RATIO, SWIPE_END_RATIO);
    }

    /**
     * Swipes down across the screen to refresh or load additional content.
     * This method is often used to perform a pull-to-refresh action or to scroll down to the bottom of a list.
     *
     * <p>Usage in a method:
     * <pre>
     * {@code
     * public FeedScreen refreshFeed() {
     *     swipeHandler.swipeDown();
     *     WebElement refreshIndicator = appiumDriver.findElement(By.id("refresh_indicator"));
     *     waitForElementToDisappear(refreshIndicator);
     *     return this;
     * }
     * }
     * </pre>
     */
    public void swipeDown() {
        swipeInDirectionWithPercentage(DOWN, SWIPE_END_RATIO, SWIPE_START_RATIO);
    }

    /**
     * Swipes left across the screen to navigate back or reveal hidden menus.
     * This method is useful for interacting with horizontally scrollable content.
     *
     * <p>Usage in a method:
     * <pre>
     * {@code
     * public SettingsScreen openHiddenMenu() {
     *     swipeHandler.swipeLeft();
     *     WebElement hiddenMenuButton = appiumDriver.findElement(By.id("hidden_menu_button"));
     *     hiddenMenuButton.click();
     *     return new SettingsScreen(appiumDriver);
     * }
     * }
     * </pre>
     */
    public void swipeLeft() {
        swipeInDirectionWithPercentage(LEFT, SWIPE_START_RATIO, SWIPE_END_RATIO);
    }

    /**
     * Swipes right across the screen to navigate to the next section of the app.
     * This method is typically used when you want to navigate through horizontally scrollable content.
     *
     * <p>Usage in a method:
     * <pre>
     * {@code
     * public ProfileScreen navigateToNextSection() {
     *     swipeHandler.swipeRight();
     *     WebElement nextSectionTitle = appiumDriver.findElement(By.id("next_section_title"));
     *     nextSectionTitle.click();
     *     return new ProfileScreen(appiumDriver);
     * }
     * }
     * </pre>
     */
    public void swipeRight() {
        swipeInDirectionWithPercentage(RIGHT, SWIPE_END_RATIO, SWIPE_START_RATIO);
    }

    /**
     * Swipes in the specified direction using the provided start and end percentages.
     * The percentages define where the swipe starts and ends on the screen.
     *
     * <p>This method allows for precise control over the swipe action by specifying the starting and ending
     * points of the swipe as percentages of the screen size. For example, if you want to swipe from the bottom
     * to the top of the screen, you can call this method with the direction set to "up", the start percentage set to 0.7,
     * and the end percentage set to 0.3. This will start the swipe at 70% of the screen's height and end it at 30%.
     * The method ensures that the swipe action is valid for the given direction by checking that the start and end
     * percentages are correctly ordered.
     *
     * <p>For example, to swipe from the bottom to the top, use "up" with startPercentage = 0.7 and endPercentage = 0.3.
     * For a swipe from the top to the bottom, use "down" with startPercentage = 0.3 and endPercentage = 0.7.
     *
     * <p>Usage Example 1:
     * <pre>
     * {@code
     * public ProductScreen navigateToProductDetails() {
     *     swipeHandler.swipeInDirectionWithPercentage("up", 0.8, 0.2);
     *     WebElement productDetailsButton = appiumDriver.findElement(By.id("product_details_button"));
     *     productDetailsButton.click();
     *     return new ProductScreen(appiumDriver);
     * }
     * }
     * </pre>
     *
     * <p>Usage Example 2:
     * <pre>
     * {@code
     * public SettingsScreen openHiddenMenu() {
     *     swipeHandler.swipeInDirectionWithPercentage("left", 0.9, 0.3);
     *     WebElement hiddenMenuButton = appiumDriver.findElement(By.id("hidden_menu_button"));
     *     hiddenMenuButton.click();
     *     return new SettingsScreen(appiumDriver);
     * }
     * }
     * </pre>
     *
     * @param direction       The direction to swipe ("up", "down", "left", "right").
     * @param startPercentage The percentage of the screen to start the swipe.
     * @param endPercentage   The percentage of the screen to end the swipe.
     * @throws IllegalArgumentException if the start and end percentages are not valid for the given direction.
     */
    public void swipeInDirectionWithPercentage(String direction, double startPercentage, double endPercentage) {
        // Validate that the percentages align with the specified direction
        switch (direction.toLowerCase()) {
            case UP:
                if (startPercentage < endPercentage) {
                    throw new IllegalArgumentException("For 'up' direction, startPercentage should be greater than endPercentage.");
                }
                break;
            case DOWN:
                if (startPercentage > endPercentage) {
                    throw new IllegalArgumentException("For 'down' direction, startPercentage should be less than endPercentage.");
                }
                break;
            case LEFT:
                if (startPercentage < endPercentage) {
                    throw new IllegalArgumentException("For 'left' direction, startPercentage should be greater than endPercentage.");
                }
                break;
            case RIGHT:
                if (startPercentage > endPercentage) {
                    throw new IllegalArgumentException("For 'right' direction, startPercentage should be less than endPercentage.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid swipe direction: " + direction);
        }

        Dimension size = appiumDriver.manage().window().getSize();

        switch (direction.toLowerCase()) {
            case UP:
                startX = size.width / 2;
                startY = (int) (size.height * startPercentage);
                endX = startX;
                endY = (int) (size.height * endPercentage);
                break;
            case DOWN:
                startX = size.width / 2;
                startY = (int) (size.height * endPercentage);
                endX = startX;
                endY = (int) (size.height * startPercentage);
                break;
            case LEFT:
                startY = size.height / 2;
                startX = (int) (size.width * startPercentage);
                endY = startY;
                endX = (int) (size.width * endPercentage);
                break;
            case RIGHT:
                startY = size.height / 2;
                startX = (int) (size.width * endPercentage);
                endY = startY;
                endX = (int) (size.width * startPercentage);
                break;
        }
        performSwipe(new Point(startX, startY), new Point(endX, endY), Duration.ofMillis(1000));
    }

    /**
     * Swipes in the specified direction multiple times using the provided start and end percentages.
     * This method is useful when you need to perform a series of swipes, such as repeatedly scrolling through content.
     *
     * <p>Usage:
     * <pre>
     * {@code
     * public ProductScreen scrollThroughProductList() {
     *     swipeHandler.swipeInDirectionWithPercentage("down", 0.8, 0.2, 3);
     *     WebElement loadMoreButton = appiumDriver.findElement(By.id("load_more_button"));
     *     loadMoreButton.click();
     *     return new ProductScreen(appiumDriver);
     * }
     * }
     * </pre>
     *
     * @param direction       The direction to swipe ("up", "down", "left", "right").
     * @param startPercentage The percentage of the screen to start the swipe.
     * @param endPercentage   The percentage of the screen to end the swipe.
     * @param numberOfSwipes  The number of times to perform the swipe.
     * @throws IllegalArgumentException if the start and end percentages are not valid for the given direction.
     */
    public void swipeInDirectionWithPercentage(String direction, double startPercentage, double endPercentage, int numberOfSwipes) {
        for (int i = 0; i < numberOfSwipes; i++) {
            swipeInDirectionWithPercentage(direction, startPercentage, endPercentage);
        }
    }

    /**
     * Swipes until the specified element is found within the given timeout.
     * This method is useful when you need to keep swiping in a direction until a specific element becomes visible.
     * The swipe will continue until the element is found or the timeout is reached.
     *
     * <p>Usage:
     * <pre>
     * {@code
     * public CatalogScreen swipeToCatalogButton() {
     *     WebElement catalogButton = appiumDriver.findElement(By.id("catalog_button"));
     *     swipeHandler.swipeUntilElementFound(catalogButton, "up", Duration.ofSeconds(10));
     *     catalogButton.click();
     *     return new CatalogScreen(appiumDriver);
     * }
     * }
     * </pre>
     *
     * @param element   The element to find.
     * @param direction The direction to swipe.
     * @param timeout   The duration to keep swiping until the element is found.
     * @throws ElementNotFoundException  If the element is not found within the timeout.
     * @throws SwipeInterruptedException If the swipe operation is interrupted.
     * @throws SwipeExecutionException   If an error occurs during the swipe operation.
     */
    public void swipeUntilElementFound(WebElement element, String direction, Duration timeout) throws ElementNotFoundException, SwipeInterruptedException, SwipeExecutionException {
        performSwipeUntilElementFound(element, direction, timeout, DEFAULT_SWIPE_DURATION, null, null);
    }

    /**
     * Swipes until the specified element is found within the given timeout, using a percentage-based swipe.
     * This method allows for more control over the swipe action by specifying the start and end percentages of the swipe.
     * It is useful when you need to perform a partial swipe or adjust the swipe length.
     *
     * <p>Usage:
     * <pre>
     * {@code
     * public CatalogScreen swipeToSpecificElement() {
     *     WebElement specificElement = appiumDriver.findElement(By.id("specific_element"));
     *     swipeHandler.swipeUntilElementFoundWithPercentage(specificElement, "down", Duration.ofSeconds(10), 0.9, 0.4);
     *     specificElement.click();
     *     return new CatalogScreen(appiumDriver);
     * }
     * }
     * </pre>
     *
     * @param element        The element to find.
     * @param direction      The direction to swipe.
     * @param timeout        The duration to keep swiping until the element is found.
     * @param startPercentage The percentage of the screen to start the swipe.
     * @param endPercentage   The percentage of the screen to end the swipe.
     * @throws ElementNotFoundException  If the element is not found within the timeout.
     * @throws SwipeInterruptedException If the swipe operation is interrupted.
     * @throws SwipeExecutionException   If an error occurs during the swipe operation.
     */
    public void swipeUntilElementFoundWithPercentage(WebElement element, String direction, Duration timeout, double startPercentage, double endPercentage) throws ElementNotFoundException, SwipeInterruptedException, SwipeExecutionException {
        performSwipeUntilElementFound(element, direction, timeout, null, startPercentage, endPercentage);
    }

    /**
     * Performs the swipe action until the specified element is found, or the timeout is reached.
     * This is an internal method that supports both duration-based and percentage-based swipes.
     *
     * @param element        The element to find.
     * @param direction      The direction to swipe.
     * @param timeout        The duration to keep swiping until the element is found.
     * @param swipeSpeed     The duration of each swipe, or null for percentage-based swipes.
     * @param startPercentage The percentage of the screen to start the swipe, or null for duration-based swipes.
     * @param endPercentage   The percentage of the screen to end the swipe, or null for duration-based swipes.
     * @throws ElementNotFoundException  If the element is not found within the timeout.
     * @throws SwipeInterruptedException If the swipe operation is interrupted.
     * @throws SwipeExecutionException   If an error occurs during the swipe operation.
     */
    private void performSwipeUntilElementFound(WebElement element, String direction, Duration timeout, Duration swipeSpeed, Double startPercentage, Double endPercentage) throws ElementNotFoundException, SwipeInterruptedException, SwipeExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        try (executor) {
            Future<Boolean> elementFoundFuture = executor.submit(() -> {
                long startTime = System.currentTimeMillis();
                long endTime = startTime + timeout.toMillis();

                while (System.currentTimeMillis() < endTime) {
                    try {
                        if (element.isDisplayed()) {
                            logger.debug("Element found.");
                            centerElement(element);
                            return true;
                        }
                    } catch (NoSuchElementException e) {
                        logger.debug("Element not found, swiping in direction: {}", direction);
                        if (swipeSpeed != null) {
                            swipeInDirection(direction);
                        } else if (startPercentage != null && endPercentage != null) {
                            swipeInDirectionWithPercentage(direction, startPercentage, endPercentage);
                        }
                        Thread.sleep(500);
                    }
                }
                logger.error("Element not found within the timeout period.");
                return false;
            });

            if (Boolean.FALSE.equals(elementFoundFuture.get(timeout.getSeconds(), TimeUnit.SECONDS))) {
                throw new TimeoutException("Element not found within the timeout period.");
            }
        } catch (TimeoutException e) {
            throw new ElementNotFoundException("Element not found within the timeout period", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SwipeInterruptedException("Thread was interrupted during swipe operation", e);
        } catch (ExecutionException e) {
            throw new SwipeExecutionException("Execution error during swipe and wait for element", e);
        }
    }

    /**
     * Swipes in the specified direction using default start and end percentages.
     * This method is typically used when the direction is known, but specific start and end points are not required.
     *
     * @param direction The direction to swipe ("up", "down", "left", "right").
     */
    private void swipeInDirection(String direction) {
        Dimension screenDimensions = appiumDriver.manage().window().getSize();
        int horizontalCenter = screenDimensions.width / 2;
        int verticalCenter = screenDimensions.height / 2;

        switch (direction.toLowerCase()) {
            case UP:
                startX = horizontalCenter;
                startY = (int) (screenDimensions.height * SWIPE_START_RATIO);
                endX = horizontalCenter;
                endY = (int) (screenDimensions.height * SWIPE_END_RATIO);
                break;
            case DOWN:
                startX = horizontalCenter;
                startY = (int) (screenDimensions.height * SWIPE_END_RATIO);
                endX = horizontalCenter;
                endY = (int) (screenDimensions.height * SWIPE_START_RATIO);
                break;
            case LEFT:
                startX = (int) (screenDimensions.width * SWIPE_START_RATIO);
                startY = verticalCenter;
                endX = (int) (screenDimensions.width * SWIPE_END_RATIO);
                endY = verticalCenter;
                break;
            case RIGHT:
                startX = (int) (screenDimensions.width * SWIPE_END_RATIO);
                startY = verticalCenter;
                endX = (int) (screenDimensions.width * SWIPE_START_RATIO);
                endY = verticalCenter;
                break;
            default:
                throw new IllegalArgumentException("Invalid swipe direction: " + direction);
        }
        performSwipe(new Point(startX, startY), new Point(endX, endY), DEFAULT_SWIPE_DURATION);
    }

    /**
     * Performs a swipe action between two points on the screen over a specified duration.
     * This method is used internally by other swipe methods to execute the swipe.
     *
     * @param startPoint    The starting point of the swipe.
     * @param endPoint      The ending point of the swipe.
     * @param swipeDuration The duration of the swipe.
     */
    private void performSwipe(Point startPoint, Point endPoint, Duration swipeDuration) {
        Sequence swipe = buildTouchSequence(startPoint, endPoint, swipeDuration);
        appiumDriver.perform(Collections.singletonList(swipe));
        logger.debug("Performed swipe from {} to {} with duration: {}", startPoint, endPoint, swipeDuration);
    }

    /**
     * Builds a touch sequence for a swipe action between two points on the screen.
     * This method is used internally to create the sequence of actions that constitute a swipe.
     *
     * @param startPoint    The starting point of the swipe.
     * @param endPoint      The ending point of the swipe.
     * @param swipeDuration The duration of the swipe.
     * @return The sequence of touch actions to perform the swipe.
     */
    private Sequence buildTouchSequence(Point startPoint, Point endPoint, Duration swipeDuration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        return new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startPoint))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(swipeDuration, PointerInput.Origin.viewport(), endPoint))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
    }

    /**
     * Centers the specified element on the screen.
     * This method is useful when you want to ensure that a specific element is centered on the screen after a swipe.
     *
     * <p>Usage:
     * <pre>
     * {@code
     * public CatalogScreen swipeToCenterCatalogButton() {
     *     WebElement catalogButton = appiumDriver.findElement(By.id("catalog_button"));
     *     swipeHandler.centerElement(catalogButton);
     *     catalogButton.click();
     *     return new CatalogScreen(appiumDriver);
     * }
     * }
     * </pre>
     *
     * @param element The element to center on the screen.
     */
    private void centerElement(WebElement element) {
        Dimension screenSize = appiumDriver.manage().window().getSize();
        Point elementLocation = element.getLocation();
        Dimension elementSize = element.getSize();

        int elementCenterX = elementLocation.getX() + elementSize.getWidth() / 2;
        int elementCenterY = elementLocation.getY() + elementSize.getHeight() / 2;

        int screenCenterX = screenSize.width / 2;
        int screenCenterY = screenSize.height / 2;

        int offsetX = screenCenterX - elementCenterX;
        int offsetY = screenCenterY - elementCenterY;

        if (Math.abs(offsetX) > 0 || Math.abs(offsetY) > 0) {
            Sequence moveToCenter = buildTouchSequence(
                    new Point(elementCenterX, elementCenterY),
                    new Point(screenCenterX, screenCenterY),
                    Duration.ofMillis(500)
            );
            appiumDriver.perform(Collections.singletonList(moveToCenter));
            logger.debug("Centered element from ({}, {}) to ({}, {}).", elementCenterX, elementCenterY, screenCenterX, screenCenterY);
        } else {
            logger.debug("Element is already centered.");
        }
    }
}
