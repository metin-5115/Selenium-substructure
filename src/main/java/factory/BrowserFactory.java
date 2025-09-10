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

public enum BrowserFactory {
    CHROME {
        @Override
        public WebDriver createDriver() {
//            WebDriverManager.chromedriver().browserInDocker().enableRecording();
            SeleniumManager.getInstance().getDriverPath(CHROME.getOptions(),false);
//            WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
//            System.setProperty("webdriver.chrome.driver","driver/chromedriver");
            return new ChromeDriver(getOptions());
        }

        //        @Override
        public ChromeOptions getOptions() {
            ChromeOptions chromeOptions = new ChromeOptions();
//            chromeOptions.addArguments("auto-open-devtools-for-tabs");
            chromeOptions.addArguments("--disable-infobars");
            chromeOptions.addArguments("'--ignore-gpu-blocklist'");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("--disable-popup-blocking");
            chromeOptions.addArguments("--incognito");
            chromeOptions.addArguments("--allow-insecure-localhost");
            chromeOptions.addArguments("--remote-allow-origins=*");
            if(PropertiesReader.getParameter("headless").equalsIgnoreCase("true")){
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

    FIREFOX {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
//            SeleniumManager.getInstance().getDriverPath(FIREFOX.getOptions(), false);
            return new FirefoxDriver(getOptions());
        }

        @Override
        public FirefoxOptions getOptions() {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if(PropertiesReader.getParameter("headless").equalsIgnoreCase("true")){
                firefoxOptions.addArguments("--headless");
            }
            return firefoxOptions;
        }
    },
    EDGE {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.getInstance(DriverManagerType.EDGE).setup();
//            SeleniumManager.getInstance().getDriverPath(EDGE.getOptions(),false);
            return new EdgeDriver(getOptions());
        }

        @Override
        public EdgeOptions getOptions() {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--start-maximized");
//            not supported headless on edge in the selenium 4.18.1
//            edgeOptions.setHeadless(Boolean.parseBoolean(PropertiesReader.getParameter("headless")));
            return edgeOptions;
        }
    },
    SAFARI {
        @Override
        public WebDriver createDriver() {
//            WebDriverManager.getInstance(DriverManagerType.SAFARI).setup();
            SeleniumManager.getInstance().getDriverPath(SAFARI.getOptions(), false);
            return new SafariDriver(getOptions());
        }

        @Override
        public SafariOptions getOptions() {
            SafariOptions safariOptions = new SafariOptions();
            safariOptions.setAutomaticInspection(false);
            if (PropertiesReader.getParameter("headless").equalsIgnoreCase("true"))
                throw new HeadlessNotSupportedException(safariOptions.getBrowserName());
            return safariOptions;
        }
    };

    public abstract WebDriver createDriver();

    public abstract AbstractDriverOptions<?> getOptions();
}
