package org.autoutils.action;

import io.appium.java_client.AppiumDriver;
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

// TODO: 26.08.2024 this keeps crashing app somehow 
public class SwipeHandler2 {

    private static final Logger logger = LoggerFactory.getLogger(SwipeHandler2.class);

    private final AppiumDriver appiumDriver;

    public SwipeHandler2(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public void swipeUntilElementFound(WebElement element, String direction, Duration timeout, Duration swipeSpeed) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Boolean> elementFoundFuture;

        try {
            elementFoundFuture = executor.submit(() -> {
                long startTime = System.currentTimeMillis();
                long endTime = startTime + timeout.toMillis();

                while (System.currentTimeMillis() < endTime) {
                    try {
                        if (element.isDisplayed()) {
                            logger.info("Element found.");
                            return true;
                        }
                    } catch (NoSuchElementException e) {
                        logger.info("Element not found, swiping in direction: {}", direction);
                        swipeInDirection(direction, swipeSpeed);
                    }
                }

                logger.error("Element not found within the timeout period.");
                return false;
            });

            if (!elementFoundFuture.get(timeout.getSeconds(), TimeUnit.SECONDS)) {
                throw new TimeoutException("Element not found within the timeout period.");
            }
        } catch (TimeoutException e) {
            logger.error("Element not found within the timeout period.");
            throw new RuntimeException("Element not found within the timeout period", e);
        } catch (Exception e) {
            logger.error("Error during swipe and wait for element.", e);
            throw new RuntimeException("Error during swipe and wait for element", e);
        } finally {
            executor.shutdownNow();
            logger.info("Swipe operation completed.");
        }
    }

    private void swipeInDirection(String direction, Duration swipeSpeed) {
        Dimension screenDimensions = appiumDriver.manage().window().getSize();
        Point startPoint, endPoint;

        switch (direction.toLowerCase()) {
            case "up":
                int horizontalCenter = screenDimensions.width / 2;
                startPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.8));
                endPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.2));
                break;
            case "down":
                horizontalCenter = screenDimensions.width / 2;
                startPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.2));
                endPoint = new Point(horizontalCenter, (int) (screenDimensions.height * 0.8));
                break;
            case "left":
                int verticalCenter = screenDimensions.height / 2;
                startPoint = new Point((int) (screenDimensions.width * 0.8), verticalCenter);
                endPoint = new Point((int) (screenDimensions.width * 0.2), verticalCenter);
                break;
            case "right":
                verticalCenter = screenDimensions.height / 2;
                startPoint = new Point((int) (screenDimensions.width * 0.2), verticalCenter);
                endPoint = new Point((int) (screenDimensions.width * 0.8), verticalCenter);
                break;
            default:
                logger.error("Invalid direction: {}", direction);
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        try {
            performSwipe(startPoint, endPoint, swipeSpeed);
        } catch (Exception e) {
            logger.error("Failed to perform swipe: {}", e.getMessage());
            throw new RuntimeException("Failed to perform swipe", e);
        }
    }

    private void performSwipe(Point startPoint, Point endPoint, Duration swipeSpeed) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startPoint))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(swipeSpeed, PointerInput.Origin.viewport(), endPoint))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        appiumDriver.perform(Collections.singletonList(swipe));
        logger.info("Performed swipe from {} to {} with speed: {}", startPoint, endPoint, swipeSpeed);
    }
}
