package org.autoutils.action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Random;

/**
 * Provides methods to interact with slider elements in a web interface, enabling actions such as moving sliders to random
 * or specific positions. This class utilizes the {@link Actions} class for simulating user interactions with slider elements.
 *
 * <p>Usage Example in a Page Object:</p>
 * <pre>{@code
 * public abstract class BasicPage extends PageSetup {
 *
 *     protected SliderHandler sliderHandler;
 *
 *     protected BasicPage(WebDriver webDriver) {
 *         super(webDriver);
 *         this.sliderHandler = new SliderHandler(webDriver);
 *     }
 * }
 * }</pre>
 */
public class SliderHandler {

    private final Actions actions;
    private final Random random;

    /**
     * Constructor to initialize {@link SliderHandler} with a {@link WebDriver} instance.
     * This ensures that all actions performed by this handler are associated with the provided WebDriver.
     *
     * @param webDriver The WebDriver instance to be used for performing actions.
     */
    public SliderHandler(WebDriver webDriver) {
        this.actions = new Actions(webDriver);
        this.random = new Random();
    }

    /**
     * Moves the slider to a random position within its entire range.
     *
     * <p>Usage Example:</p>
     * <pre>{@code
     * public void setRatingToRandomValue(WebElement sliderThumb, WebElement slider, int minValue, int maxValue) {
     *     sliderHandler.moveSliderRandomly(sliderThumb, slider, minValue, maxValue);
     * }
     * }</pre>
     *
     * @param sliderThumb The slider thumb {@link WebElement} to be moved.
     * @param sliderBar      The slider {@link WebElement} representing the entire slider.
     * @param minValue    The minimum value of the slider's range.
     * @param maxValue    The maximum value of the slider's range.
     */
    public void moveSliderRandomly(WebElement sliderThumb, WebElement sliderBar, int minValue, int maxValue) {
        int offset = calculateRandomHorizontalOffset(sliderBar, minValue, maxValue);
        actions.dragAndDropBy(sliderThumb, offset, 0).perform();
    }

    /**
     * Overloaded method that moves the slider to a random position, accepting minimum and maximum values as Strings.
     * This method parses the String inputs to integers and then delegates to the main method.
     *
     * <p>Usage Example:</p>
     * <pre>{@code
     * public void setRandomValueFromStrings(WebElement sliderThumb, WebElement sliderBar) {
     *     sliderHandler.moveSliderRandomly(sliderThumb, sliderBar, "0", "10");
     * }
     * }</pre>
     *
     * @param sliderThumb The slider thumb {@link WebElement} to be moved.
     * @param sliderBar   The slider {@link WebElement} representing the entire slider.
     * @param minValue    The minimum value of the slider's range as a String.
     * @param maxValue    The maximum value of the slider's range as a String.
     */
    public void moveSliderRandomly(WebElement sliderThumb, WebElement sliderBar, String minValue, String maxValue) {
        moveSliderRandomly(sliderThumb, sliderBar, Integer.parseInt(minValue), Integer.parseInt(maxValue));
    }

    /**
     * Overloaded method that moves the slider to a random position within its entire range.
     * This method automatically retrieves the min and max values from the slider element.
     *
     * <p>Usage Example:</p>
     * <pre>{@code
     * public void setRatingToRandomValue(WebElement sliderThumb, WebElement sliderBar) {
     *     sliderHandler.moveSliderRandomly(sliderThumb, sliderBar);
     * }
     * }</pre>
     *
     * @param sliderThumb The slider thumb {@link WebElement} to be moved.
     * @param sliderBar   The slider {@link WebElement} representing the entire slider.
     */
    public void moveSliderRandomly(WebElement sliderThumb, WebElement sliderBar) {
        moveSliderRandomly(sliderThumb, sliderBar, getMinValue(sliderBar), getMaxValue(sliderBar));
    }

    /**
     * Moves the slider to the specified value within its range.
     *
     * <p>Usage Example:</p>
     * <pre>{@code
     * public void setRatingToFiveStars(WebElement sliderThumb, WebElement slider, int minValue, int maxValue) {
     *     int desiredValue = 5;
     *     sliderHandler.moveSlider(sliderThumb, slider, minValue, maxValue, desiredValue);
     * }
     * }</pre>
     *
     * @param sliderThumb  The slider thumb {@link WebElement} to be moved.
     * @param sliderBar       The slider {@link WebElement} representing the entire slider.
     * @param minValue     The minimum value of the slider's range.
     * @param maxValue     The maximum value of the slider's range.
     * @param desiredValue The target value to set the slider to.
     */
    public void moveSlider(WebElement sliderThumb, WebElement sliderBar, int minValue, int maxValue, int desiredValue) {
        int offset = calculateHorizontalOffset(sliderBar, minValue, maxValue, desiredValue);
        actions.dragAndDropBy(sliderThumb, offset, 0).perform();
    }

    /**
     * Moves the slider to the minimum value within its range.
     * This method automatically retrieves the min and max values from the slider element.
     *
     * <p>Usage Example:</p>
     * <pre>{@code
     * public void moveSliderToMin(WebElement sliderThumb, WebElement sliderBar) {
     *     sliderHandler.moveSliderToMinimumValue(sliderThumb, sliderBar);
     * }
     * }</pre>
     *
     * @param sliderThumb The slider thumb {@link WebElement} to be moved.
     * @param sliderBar   The slider {@link WebElement} representing the entire slider.
     */
    public void moveSliderToMinimumValue(WebElement sliderThumb, WebElement sliderBar) {
        moveSlider(sliderThumb, sliderBar, getMinValue(sliderBar), getMaxValue(sliderBar), getMinValue(sliderBar));
    }

    /**
     * Moves the slider to the maximum value within its range.
     * This method automatically retrieves the min and max values from the slider element.
     *
     * <p>Usage Example:</p>
     * <pre>{@code
     * public void moveSliderToMax(WebElement sliderThumb, WebElement sliderBar) {
     *     sliderHandler.moveSliderToMaximumValue(sliderThumb, sliderBar);
     * }
     * }</pre>
     *
     * @param sliderThumb The slider thumb {@link WebElement} to be moved.
     * @param sliderBar   The slider {@link WebElement} representing the entire slider.
     */
    public void moveSliderToMaximumValue(WebElement sliderThumb, WebElement sliderBar) {
        moveSlider(sliderThumb, sliderBar, getMinValue(sliderBar), getMaxValue(sliderBar), getMaxValue(sliderBar));
    }

    /**
     * Calculates a random horizontal offset within the slider's range.
     *
     * @param sliderBar   The slider {@link WebElement}.
     * @param minValue The minimum value of the slider's range.
     * @param maxValue The maximum value of the slider's range.
     * @return The calculated horizontal offset in pixels within the slider's range.
     */
    private int calculateRandomHorizontalOffset(WebElement sliderBar, int minValue, int maxValue) {
        int randomValue = getRandomInclusive(minValue, maxValue);
        return calculateHorizontalOffset(sliderBar, minValue, maxValue, randomValue);
    }

    /**
     * Calculates a horizontal offset for moving the slider to a specific desired value.
     *
     * @param sliderBar       The slider {@link WebElement}.
     * @param minValue     The minimum value of the slider's range.
     * @param maxValue     The maximum value of the slider's range.
     * @param desiredValue The target value to set the slider to.
     * @return The calculated horizontal offset in pixels to achieve the desired value.
     */
    private int calculateHorizontalOffset(WebElement sliderBar, int minValue, int maxValue, int desiredValue) {
        int sliderWidth = sliderBar.getSize().getWidth();
        return sliderWidth * (desiredValue - minValue) / (maxValue - minValue);
    }

    /**
     * Generates a random integer between the specified minimum and maximum values, inclusive, using {@link Random#nextInt(int)}.
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @return A random integer between min and max, inclusive.
     */
    private int getRandomInclusive(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Retrieves the minimum value of the slider's range by reading the "min" attribute of the slider element.
     *
     * @param sliderBar The slider {@link WebElement}.
     * @return The minimum value of the slider's range.
     */
    private int getMinValue(WebElement sliderBar) {
        return Integer.parseInt(sliderBar.getAttribute("min"));
    }

    /**
     * Retrieves the maximum value of the slider's range by reading the "max" attribute of the slider element.
     *
     * @param sliderBar The slider {@link WebElement}.
     * @return The maximum value of the slider's range.
     */
    private int getMaxValue(WebElement sliderBar) {
        return Integer.parseInt(sliderBar.getAttribute("max"));
    }
    // TODO: 28.08.2024 Change names of elements: sliderThumb - ok, slider, change to sliderBar - done
    // TODO: 28.08.2024 Add getting min and max values automatically from sliderBar - done
    // TODO: 28.08.2024 Add methods that automatically move slider to min value and another to max value - done
    // TODO: 28.08.2024 Overload methods, so they would accept String values - parse String to int inside - done
    // TODO: 28.08.2024 Add missing javadocs
}
