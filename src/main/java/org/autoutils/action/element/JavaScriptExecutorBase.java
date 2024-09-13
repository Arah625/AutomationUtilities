package org.autoutils.action.element;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

// TODO: 13.09.2024 Find package to place that class
public abstract class JavaScriptExecutorBase {

    protected final RemoteWebDriver driver;
    protected final JavascriptExecutor jsExecutor;

    public JavaScriptExecutorBase(RemoteWebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    /**
     * Executes the given JavaScript script with the provided arguments.
     *
     * @param script the JavaScript code to execute
     * @param args the arguments to pass to the script
     * @return the result of the script execution
     */
    protected Object executeScript(String script, Object... args) {
        return jsExecutor.executeScript(script, args);
    }
}