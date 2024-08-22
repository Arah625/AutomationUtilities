/**
 * Provides comprehensive utilities for implementing wait strategies in web automation tests.
 * This package is designed to facilitate waiting for specific conditions to be met in web pages, enhancing the reliability and robustness of tests.
 * It includes sub-packages and classes focused on different aspects of waiting mechanisms, from waiting on web elements to locators, and offers a foundational structure for custom wait conditions.
 * <p>
 * Key components of this package include:
 * <ul>
 *   <li>{@link org.autoutils.detection.wait.element} sub-package -
 *       Contains utility classes for waiting on specific conditions of web elements within a webpage.
 *       It offers focused methods for handling common visibility wait scenarios encountered in web automation testing with web elements.</li>
 *   <li>{@link org.autoutils.detection.wait.locator} sub-package -
 *       Provides utility classes for waiting on conditions related to locators, allowing for flexible wait strategies when dealing with elements identified by locators.
 *       This is particularly useful for dynamic content or when multiple potential elements could signal readiness to proceed.</li>
 * </ul>
 * <p>
 * The utilities within this package leverage {@link org.openqa.selenium.support.ui.FluentWait} to offer customizable wait strategies,
 * including the ability to set timeouts and polling intervals. This enables tests to wait dynamically for conditions to be met, ensuring elements or conditions
 * are ready for interaction before proceeding with further actions. The aim is to reduce flakiness and improve the consistency of automated web tests.
 */
package org.autoutils.detection.wait;
