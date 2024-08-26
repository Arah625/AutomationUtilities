package org.autoutils.action;

import io.appium.java_client.AppiumDriver;
import org.autoutils.action.exception.ElementNotFoundException;
import org.autoutils.action.exception.SwipeOperationException;
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

public class SwipeHandler {

    private static final Logger logger = LoggerFactory.getLogger(SwipeHandler.class);
    private static final Duration DEFAULT_SWIPE_DURATION = Duration.ofMillis(500);

    private final AppiumDriver appiumDriver;

    public SwipeHandler(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public void swipeUntilElementFound(WebElement element, String direction, Duration timeout) {
        performSwipeUntilElementFound(element, direction, timeout, DEFAULT_SWIPE_DURATION, null, null);
    }

    public void swipeUntilElementFoundWithPercentage(WebElement element, String direction, Duration timeout, double startPercentage, double endPercentage) {
        performSwipeUntilElementFound(element, direction, timeout, null, startPercentage, endPercentage);
    }

    private void performSwipeUntilElementFound(WebElement element, String direction, Duration timeout, Duration swipeSpeed, Double startPercentage, Double endPercentage) {
        try (ExecutorService executor = Executors.newFixedThreadPool(1)) {
            Future<Boolean> elementFoundFuture = executor.submit(() -> {
                long endTime = System.currentTimeMillis() + timeout.toMillis();

                while (System.currentTimeMillis() < endTime) {
                    try {
                        if (element.isDisplayed()) {
                            logger.info("Element found.");
                            centerElement(element);
                            return true;
                        }
                    } catch (NoSuchElementException e) {
                        logger.info("Element not found, swiping in direction: {}", direction);
                        if (swipeSpeed != null) {
                            swipeInDirection(direction, swipeSpeed);
                        } else if (startPercentage != null && endPercentage != null) {
                            swipeInDirectionWithPercentage(direction, startPercentage, endPercentage);
                        }

                        Thread.sleep(500); // Give time for UI updates
                    }
                }

                logger.error("Element not found within the timeout period.");
                return false;
            });

            if (Boolean.FALSE.equals(elementFoundFuture.get(timeout.getSeconds(), TimeUnit.SECONDS))) {
                throw new ElementNotFoundException("Element not found within the timeout period.");
            }
        } catch (TimeoutException e) {
            throw new ElementNotFoundException("Element not found within the timeout period", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new SwipeOperationException("Thread was interrupted during swipe operation", e);
        } catch (ExecutionException e) {
            throw new SwipeOperationException("Execution exception occurred during swipe and wait for element", e);
        } finally {
            logger.info("Swipe operation completed.");
        }
    }

    private void swipeInDirection(String direction, Duration swipeSpeed) {
        Dimension screenDimensions = appiumDriver.manage().window().getSize();
        int horizontalCenter = screenDimensions.width / 2;
        int verticalCenter = screenDimensions.height / 2;

        Point startPoint;
        Point endPoint;

        switch (direction.toLowerCase()) {
            case "up":
                startPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.8));
                endPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.2));
                break;
            case "down":
                startPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.2));
                endPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.8));
                break;
            case "left":
                startPoint = new Point((int) (screenDimensions.width * 0.8), verticalCenter);
                endPoint = new Point((int) (screenDimensions.width * 0.2), verticalCenter);
                break;
            case "right":
                startPoint = new Point((int) (screenDimensions.width * 0.2), verticalCenter);
                endPoint = new Point((int) (screenDimensions.width * 0.8), verticalCenter);
                break;
            default:
                throw new IllegalArgumentException("Invalid swipe direction: " + direction);
        }

        performSwipe(startPoint, endPoint, swipeSpeed != null ? swipeSpeed : DEFAULT_SWIPE_DURATION);
    }

    private void swipeInDirectionWithPercentage(String direction, double startPercentage, double endPercentage) {
        Dimension size = appiumDriver.manage().window().getSize();

        int startX;
        int startY;
        int endX;
        int endY;

        switch (direction.toLowerCase()) {
            case "up":
                startX = size.width / 2;
                startY = (int) (size.height * startPercentage);
                endX = startX;
                endY = (int) (size.height * endPercentage);
                break;
            case "down":
                startX = size.width / 2;
                startY = (int) (size.height * endPercentage);
                endX = startX;
                endY = (int) (size.height * startPercentage);
                break;
            case "left":
                startY = size.height / 2;
                startX = (int) (size.width * startPercentage);
                endY = startY;
                endX = (int) (size.width * endPercentage);
                break;
            case "right":
                startY = size.height / 2;
                startX = (int) (size.width * endPercentage);
                endY = startY;
                endX = (int) (size.width * startPercentage);
                break;
            default:
                throw new IllegalArgumentException("Invalid swipe direction: " + direction);
        }

        performSwipe(new Point(startX, startY), new Point(endX, endY), Duration.ofMillis(1000));
    }

    private void performSwipe(Point startPoint, Point endPoint, Duration swipeDuration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startPoint))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(swipeDuration, PointerInput.Origin.viewport(), endPoint))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        appiumDriver.perform(Collections.singletonList(swipe));
        logger.info("Performed swipe from {} to {} with duration: {}", startPoint, endPoint, swipeDuration);
    }

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
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence moveToCenter = new Sequence(finger, 0)
                    .addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), new Point(elementCenterX, elementCenterY)))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), new Point(screenCenterX, screenCenterY)))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            appiumDriver.perform(Collections.singletonList(moveToCenter));
            logger.info("Centered element from ({}, {}) to ({}, {}).", elementCenterX, elementCenterY, screenCenterX, screenCenterY);
        } else {
            logger.info("Element is already centered.");
        }
    }
}
