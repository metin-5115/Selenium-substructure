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

/**
 * TargetFactory
 *
 * Purpose:
 *  - Creates WebDriver instances depending on the execution target:
 *    LOCAL  → run on local machine (via BrowserFactory).
 *    REMOTE → run on Selenium Grid / Docker (via RemoteWebDriver).
 *
 * Usage:
 *   new TargetFactory().createInstance("chrome");
 *
 * Dependencies:
 *  - PropertiesReader → reads "target" property (local | remote).
 *  - BrowserFactory   → creates local WebDrivers (Chrome, Firefox, etc.).
 *  - RemoteWebDriver  → connects to Selenium Grid for remote execution.
 */
public class TargetFactory {

    private static final Logger logger = LogManager.getLogger(TargetFactory.class);

    /**
     * Creates a WebDriver instance based on configuration.
     *
     * @param browser browser name (e.g., chrome, firefox, edge, safari)
     * @return WebDriver instance ready for use
     */
    public WebDriver createInstance(String browser) {
        // Reads "target" property from configuration.properties or system property
        Target target = Target.valueOf(PropertiesReader.getParameter("target").toUpperCase());
        WebDriver webdriver;

        switch (target) {
            case LOCAL:
                // Delegates to BrowserFactory (enum) for local drivers
                webdriver = BrowserFactory.valueOf(browser.toUpperCase()).createDriver();
                break;

            case REMOTE:
                // Creates a RemoteWebDriver connected to Selenium Grid
                webdriver = createRemoteInstance();
                break;

            default:
                // If invalid target → throw custom exception
                throw new TargetNotValidException(target.toString());
        }

        return webdriver;
    }

    /**
     * Creates a RemoteWebDriver instance to connect to Selenium Grid.
     * Uses ChromeOptions but (note: caps.setBrowserName is currently "firefox", which may be inconsistent).
     *
     * @return RemoteWebDriver connected to Grid
     */
    private RemoteWebDriver createRemoteInstance() {
        RemoteWebDriver remoteWebDriver = null;
        DesiredCapabilities caps = new DesiredCapabilities();

        // Browser-specific options (Chrome in this case)
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

        // 🚨 Potential bug: here you set Firefox instead of Chrome
        caps.setBrowserName("firefox");

        try {
            // Grid URL is read from configuration.properties (key: grid)
            String gridURL = String.format("%s", PropertiesReader.getParameter("grid"));

            // Create RemoteWebDriver with provided capabilities
            remoteWebDriver = new RemoteWebDriver(new URL(gridURL), caps);

        } catch (java.net.MalformedURLException e) {
            System.out.println("Grid URL is invalid or Grid is not available");
        } catch (IllegalArgumentException e) {
            System.out.println("Browser capabilities are not valid or recognized");
        }

        return remoteWebDriver;
    }

    /**
     * Execution target types: LOCAL (run on developer machine), REMOTE (Grid/Docker).
     */
    enum Target {
        LOCAL, REMOTE
    }
}
