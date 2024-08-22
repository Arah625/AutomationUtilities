/**
 * Provides utility classes for waiting on specific conditions of locators within a webpage or mobile screen.
 * This package includes classes, offering focused methods to handle common visibility wait scenarios
 * encountered in automation testing when dealing with locators.
 * <p>
 * The two main classes within this package are:
 * <ul>
 *   <li>{@link org.autoutils.detection.wait.locator.WaitForAllLocators} -
 *       Allows waiting for all elements identified by the specified locators to become visible simultaneously before proceeding.
 *       This class is beneficial for scenarios where the test depends on the visibility of multiple elements at the same time,
 *       such as confirming that various parts of a page have loaded completely.</li>
 *   <li>{@link org.autoutils.detection.wait.locator.WaitForFirstLocator} -
 *       Facilitates waiting for the visibility of the first element identified by any one of a given set of locators.
 *       This class is applicable in situations where multiple potential outcomes are possible, and the visibility of
 *       any single element identified by these locators is sufficient to proceed with the test steps.</li>
 * </ul>
 * <p>
 * These utilities leverage {@link org.openqa.selenium.support.ui.FluentWait} to provide flexible wait strategies,
 * allowing customization of timeout and polling intervals to suit various testing conditions. The intention is to enhance
 * the reliability of tests by ensuring that elements identified by the locators are ready for interaction as expected before
 * actions are performed on them.
 */
package org.autoutils.detection.wait.locator;