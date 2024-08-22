/**
 * Provides utility classes for waiting on specific conditions of web elements within a web page/screen.
 * This package includes classes, offering focused methods to handle common visibility wait scenarios
 * encountered in automation testing.
 * <p>
 * The two main classes within this package are:
 * <ul>
 *   <li>{@link org.autoutils.detection.wait.element.WaitForAllElements} -
 *       Allows waiting for all specified web elements to become visible simultaneously before proceeding.
 *       This class is useful for scenarios where the test depends on the visibility of multiple elements at the same time,
 *       such as ensuring a group of related options or controls is available to the user.</li>
 *   <li>{@link org.autoutils.detection.wait.element.WaitForFirstElement} -
 *       Facilitates waiting for the visibility of the first element from a given set.
 *       This class is applicable in situations where multiple potential outcomes are possible, and the visibility of
 *       any single element is sufficient to proceed with the test steps.</li>
 * </ul>
 * <p>
 * These utilities leverage {@link org.openqa.selenium.support.ui.FluentWait} to provide flexible wait strategies,
 * allowing customization of timeout and polling intervals to suit various test conditions. The intention is to enhance
 * the reliability of tests by ensuring elements are interactable as expected before actions are performed on them.
 */
package org.autoutils.detection.wait.element;