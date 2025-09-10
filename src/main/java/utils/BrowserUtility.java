package utils;

import factory.DriverFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserUtility {

    public String  getBrowserName(){
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        return browserName;
    }

    public String getBrowserOs(){
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        String os = cap.getPlatformName().toString().toLowerCase();
        return os;
    }

    public String getBrowserVersion(){
        Capabilities cap = ((RemoteWebDriver) DriverFactory.getDriver()).getCapabilities();
        String version = cap.getBrowserVersion();
        return version;
    }

}
