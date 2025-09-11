package utils;

import factory.DriverFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * BrowserUtility
 *
 * Purpose:
 *  - Provides helper methods to retrieve browser/environment details
 *    from the current WebDriver session.
 *  - Useful for logging, reporting, and debugging test runs.
 *
 * Data returned:
 *  - Browser name (e.g., chrome, firefox, edge, safari)
 *  - Operating system (e.g., windows, mac, linux)
 *  - Browser version (e.g., 114.0, 115.2)
 *
 * Example Usage:
 *   BrowserUtility util = new BrowserUtility();
 *   String browser = util.getBrowserName();   // "chrome"
 *   String os = util.getBrowserOs();          // "windows"
 *   String version = util.getBrowserVersion();// "114.0"
 */
public class BrowserUtility {

    /**
     * Returns the current browser name in lowercase.
     * Example: "chrome", "firefox".
     */
    public String getBrowserName(){
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        return cap.getBrowserName().toLowerCase();
    }

    /**
     * Returns the operating system of the browser in lowercase.
     * Example: "windows", "mac", "linux".
     */
    public String getBrowserOs(){
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        return cap.getPlatformName().toString().toLowerCase();
    }

    /**
     * Returns the browser version as a string.
     * Example: "114.0", "115.2".
     */
    public String getBrowserVersion(){
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        return cap.getBrowserVersion();
    }
}
