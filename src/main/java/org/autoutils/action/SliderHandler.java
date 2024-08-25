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
     * @param slider      The slider {@link WebElement} representing the entire slider.
     * @param minValue    The minimum value of the slider's range.
     * @param maxValue    The maximum value of the slider's range.
     */
    public void moveSliderRandomly(WebElement sliderThumb, WebElement slider, int minValue, int maxValue) {
        int offset = calculateRandomHorizontalOffset(slider, minValue, maxValue);
        actions.dragAndDropBy(sliderThumb, offset, 0).perform();
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
     * @param slider       The slider {@link WebElement} representing the entire slider.
     * @param minValue     The minimum value of the slider's range.
     * @param maxValue     The maximum value of the slider's range.
     * @param desiredValue The target value to set the slider to.
     */
    public void moveSlider(WebElement sliderThumb, WebElement slider, int minValue, int maxValue, int desiredValue) {
        int offset = calculateHorizontalOffset(slider, minValue, maxValue, desiredValue);
        actions.dragAndDropBy(sliderThumb, offset, 0).perform();
    }

    /**
     * Calculates a random horizontal offset within the slider's range.
     *
     * @param slider   The slider {@link WebElement}.
     * @param minValue The minimum value of the slider's range.
     * @param maxValue The maximum value of the slider's range.
     * @return The calculated horizontal offset in pixels within the slider's range.
     */
    private int calculateRandomHorizontalOffset(WebElement slider, int minValue, int maxValue) {
        int randomValue = getRandomInclusive(minValue, maxValue);
        return calculateHorizontalOffset(slider, minValue, maxValue, randomValue);
    }

    /**
     * Calculates a horizontal offset for moving the slider to a specific desired value.
     *
     * @param slider       The slider {@link WebElement}.
     * @param minValue     The minimum value of the slider's range.
     * @param maxValue     The maximum value of the slider's range.
     * @param desiredValue The target value to set the slider to.
     * @return The calculated horizontal offset in pixels to achieve the desired value.
     */
    private int calculateHorizontalOffset(WebElement slider, int minValue, int maxValue, int desiredValue) {
        int sliderWidth = slider.getSize().getWidth();
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
}
