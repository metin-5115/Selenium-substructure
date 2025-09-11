package factory;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * DriverFactory
 *
 * Purpose:
 *  - Manages WebDriver instances using ThreadLocal.
 *  - Ensures thread-safe driver management for parallel test execution.
 *  - Provides helper methods to set/get/quit driver instances.
 *  - Provides capability info (browser, version, platform) for reporting.
 *
 * Why ThreadLocal?
 *  - Each test thread gets its own WebDriver instance.
 *  - Prevents driver conflicts when running tests in parallel.
 *
 * Usage:
 *   DriverFactory factory = new DriverFactory();
 *   factory.setDriver(new ChromeDriver());
 *   WebDriver driver = DriverFactory.getDriver();
 *   factory.quit();
 */
public class DriverFactory {

    // Thread-safe storage for WebDriver instance per test thread
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance for the current thread.
     * Static so that it can be called without creating a new DriverFactory.
     */
    public synchronized static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Sets the WebDriver instance for the current thread.
     *
     * @param driver WebDriver instance (e.g., ChromeDriver, FirefoxDriver, RemoteWebDriver)
     * @return the same driver instance for chaining
     */
    public synchronized WebDriver setDriver(WebDriver driver) {
        this.driver.set(driver);
        return this.driver.get();
    }

    /**
     * Quits the current thread's WebDriver session and removes it from ThreadLocal.
     * Prevents memory leaks by cleaning up the driver reference.
     */
    public synchronized void quit() {
        this.driver.get().quit();
        this.driver.remove();
    }

    /**
     * Returns a string with the current WebDriver capabilities:
     *   - Browser name
     *   - Browser version
     *   - Platform name
     *
     * Useful for logs, reports, and debugging test runs.
     *
     * @return formatted capability string
     *   Example: "browser: chrome v: 114.0 platform: windows"
     */
    public synchronized String getInfo() {
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName();
        String platform = cap.getPlatformName().toString();
        String version = cap.getBrowserVersion();
        return String.format("browser: %s v: %s platform: %s", browserName, version, platform);
    }
}
