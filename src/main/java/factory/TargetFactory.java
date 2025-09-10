package factory;

import exception.TargetNotValidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.PropertiesReader;

import java.net.URL;

public class TargetFactory {

    private static final Logger logger = LogManager.getLogger(TargetFactory.class);

    public WebDriver createInstance(String browser) {
        Target target = Target.valueOf(PropertiesReader.getParameter("target").toUpperCase());
        WebDriver webdriver;
        switch (target) {
            case LOCAL:
                webdriver = BrowserFactory.valueOf(browser.toUpperCase()).createDriver();
                break;
            case REMOTE:
                webdriver = createRemoteInstance();
                break;
            default:
                throw new TargetNotValidException(target.toString());
        }
        return webdriver;
    }

    private RemoteWebDriver createRemoteInstance() {
        RemoteWebDriver remoteWebDriver = null;
        DesiredCapabilities caps = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("'--ignore-gpu-blocklist'");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--allow-insecure-localhost");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        caps.setBrowserName("firefox");
        try {
            String gridURL = String.format("%s", PropertiesReader.getParameter("grid"));
            remoteWebDriver = new RemoteWebDriver(new URL(gridURL), caps);
        } catch (java.net.MalformedURLException e) {
            System.out.println("Grid URL is invalid or Grid is not available");
//            System.out.println(String.format("Browser: %s", cap.get(CapabilityType.BROWSER_NAME)));
        } catch (IllegalArgumentException e) {
//            System.out.println(String.format("Browser %s is not valid or recognized", desiredCapabilities.get(CapabilityType.BROWSER_NAME)));
        }

        return remoteWebDriver;
    }

    enum Target {
        LOCAL, REMOTE
    }
}
