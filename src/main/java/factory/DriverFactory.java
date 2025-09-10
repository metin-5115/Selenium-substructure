package factory;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public synchronized static WebDriver getDriver() {
        return driver.get();
    }

    public synchronized WebDriver setDriver(WebDriver driver) {
        this.driver.set(driver);
        return this.driver.get();
    }
    public synchronized void quit() {
        this.driver.get().quit();
        this.driver.remove();
    }

    public synchronized String getInfo() {
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName();
        String platform = cap.getPlatformName().toString();
        String version = cap.getBrowserVersion();
        return String.format("browser: %s v: %s platform: %s", browserName, version, platform);
    }


}
