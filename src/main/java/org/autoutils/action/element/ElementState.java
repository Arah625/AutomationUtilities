package org.autoutils.action.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Defines the contract for retrieving the state of an element, allowing flexibility to check based on attributes or classes.
 */
public interface ElementState {

    /**
     * Retrieves the state of the specified WebElement by evaluating the specified attribute.
     *
     * @param webElement The WebElement whose state is to be determined.
     * @param attribute The attribute to evaluate.
     * @return A string representing the element's attribute value.
     */
    String getElementStateByAttribute(WebElement webElement, String attribute);

    /**
     * Retrieves the state of the specified WebElement by evaluating the presence of a class.
     *
     * @param webElement The WebElement whose state is to be determined.
     * @param className The class name to evaluate.
     * @return true if the class is present, false otherwise.
     */
    boolean isClassPresent(WebElement webElement, String className);

    /**
     * Retrieves the state of the element identified by the given locator by evaluating the specified attribute.
     *
     * @param locator The By locator used to find the element.
     * @param attribute The attribute to evaluate.
     * @return A string representing the element's attribute value.
     */
    String getElementStateByAttribute(By locator, String attribute);

    /**
     * Retrieves the state of the element identified by the given locator by evaluating the presence of a class.
     *
     * @param locator The By locator used to find the element.
     * @param className The class name to evaluate.
     * @return true if the class is present, false otherwise.
     */
    boolean isClassPresent(By locator, String className);
}
