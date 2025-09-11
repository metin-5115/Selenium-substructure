package factory;

import exception.HeadlessNotSupportedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import utils.PropertiesReader;

/**
 * BrowserFactory
 *
 * Enum-based factory for creating WebDriver instances per browser type.
 * Supported browsers: CHROME, FIREFOX, EDGE, SAFARI.
 *
 * Responsibilities:
 *  - Encapsulates browser-specific WebDriver creation logic.
 *  - Provides customized Options (ChromeOptions, FirefoxOptions, etc.).
 *  - Handles headless mode support (enabled where possible, blocked otherwise).
 *
 * Usage:
 *   BrowserFactory.CHROME.createDriver();
 *   BrowserFactory.FIREFOX.getOptions();
 */
public enum BrowserFactory {

    // -------------------------------
    // Chrome Browser
    // -------------------------------
    CHROME {
        @Override
        public WebDriver createDriver() {
            // Option 1: run ChromeDriver in Docker with recording (commented)
            // WebDriverManager.chromedriver().browserInDocker().enableRecording();

            // Option 2: let SeleniumManager handle driver path resolution
            SeleniumManager.getInstance().getDriverPath(CHROME.getOptions(), false);

            // Option 3: use WebDriverManager setup (commented)
            // WebDriverManager.getInstance(DriverManagerType.CHROME).setup();

            // Option 4: manual driver path (commented)
            // System.setProperty("webdriver.chrome.driver","driver/chromedriver");

            return new ChromeDriver(getOptions());
        }

        @Override
        public ChromeOptions getOptions() {
            ChromeOptions chromeOptions = new ChromeOptions();
            // Common arguments to improve stability / disable unwanted UI
            chromeOptions.addArguments("--disable-infobars");
            chromeOptions.addArguments("'--ignore-gpu-blocklist'");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("--disable-popup-blocking");
            chromeOptions.addArguments("--incognito");
            chromeOptions.addArguments("--allow-insecure-localhost");
            chromeOptions.addArguments("--remote-allow-origins=*");

            // Headless configuration if enabled in properties
            if (PropertiesReader.getParameter("headless").equalsIgnoreCase("true")) {
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--dns-prefetch-disable");
            }
            return chromeOptions;
        }
    },

    // -------------------------------
    // Firefox Browser
    // -------------------------------
    FIREFOX {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
            // SeleniumManager alternative (commented)
            // SeleniumManager.getInstance().getDriverPath(FIREFOX.getOptions(), false);
            return new FirefoxDriver(getOptions());
        }

        @Override
        public FirefoxOptions getOptions() {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if (PropertiesReader.getParameter("headless").equalsIgnoreCase("true")) {
                firefoxOptions.addArguments("--headless");
            }
            return firefoxOptions;
        }
    },

    // -------------------------------
    // Edge Browser
    // -------------------------------
    EDGE {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.getInstance(DriverManagerType.EDGE).setup();
            // SeleniumManager alternative (commented)
            // SeleniumManager.getInstance().getDriverPath(EDGE.getOptions(), false);
            return new EdgeDriver(getOptions());
        }

        @Override
        public EdgeOptions getOptions() {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--start-maximized");

            // NOTE: Selenium 4.18.1 does not support Edge headless properly
            // edgeOptions.setHeadless(Boolean.parseBoolean(PropertiesReader.getParameter("headless")));
            return edgeOptions;
        }
    },

    // -------------------------------
    // Safari Browser
    // -------------------------------
    SAFARI {
        @Override
        public WebDriver createDriver() {
            // WebDriverManager not commonly used for Safari, rely on SeleniumManager
            SeleniumManager.getInstance().getDriverPath(SAFARI.getOptions(), false);
            return new SafariDriver(getOptions());
        }

        @Override
        public SafariOptions getOptions() {
            SafariOptions safariOptions = new SafariOptions();
            safariOptions.setAutomaticInspection(false);

            // Safari does not support headless mode
            if (PropertiesReader.getParameter("headless").equalsIgnoreCase("true"))
                throw new HeadlessNotSupportedException(safariOptions.getBrowserName());

            return safariOptions;
        }
    };

    // -------------------------------
    // Abstract methods to be implemented by each browser
    // -------------------------------

    /**
     * Creates a new WebDriver instance for the browser type.
     */
    public abstract WebDriver createDriver();

    /**
     * Returns browser-specific Options object (e.g., ChromeOptions).
     */
    public abstract AbstractDriverOptions<?> getOptions();
}
