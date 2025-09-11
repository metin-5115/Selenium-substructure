package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * PropertiesReader
 *
 * Purpose:
 *  - Loads configuration values from `configuration.properties` file.
 *  - Allows overriding values via system properties (`-Dkey=value`).
 *  - Provides helper methods to get and set parameters.
 *
 * Default file location:
 *   src/test/resources/configuration.properties
 *
 * Example configuration.properties:
 *   browser=chrome
 *   target=local
 *   environment=qa
 *   grid=http://localhost:port/wd/hub
 *
 * Example usage:
 *   String browser = PropertiesReader.getParameter("browser"); // "chrome"
 *   String env = PropertiesReader.getParameter("environment"); // "qa"
 *
 *   // Override from command line:
 *   mvn test -Dbrowser=firefox -Dtarget=remote
 */
public class PropertiesReader {

    private static Properties properties;

    // Path to configuration file
    public static String file = "src"
            + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "configuration.properties";

    // Static initializer block → loads properties when class is loaded
    static {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            // Load using default encoding
            properties.load(fileInputStream);

            // Load again with UTF-8 encoding to handle special chars (Turkish, etc.)
            properties.load(new InputStreamReader(fileInputStream, Charset.forName("UTF-8")));

            fileInputStream.close();
        } catch (IOException e) {
            // Suppressed exception – better: log it instead of ignoring
            e.getMessage();
        }
    }

    /**
     * Get a parameter value.
     * Priority:
     *   1. System property (-Dkey=value passed via Maven/CLI)
     *   2. configuration.properties file
     *
     * @param parameter key to search
     * @return value of property (or null if not found)
     */
    public static String getParameter(String parameter){
        if (System.getProperty(parameter) != null) {
            return System.getProperty(parameter);
        } else {
            return properties.getProperty(parameter);
        }
    }

    /**
     * Set a parameter value dynamically.
     * If provided via system property, update it there.
     * Otherwise, update in loaded properties object.
     *
     * @param key   property key
     * @param value property value
     * @return the stored value
     */
    public static String setParameter(String key, String value){
        if (System.getProperty(value) != null) {
            System.setProperty(key, value);
            return System.getProperty(key);
        } else {
            properties.setProperty(key, value);
            return properties.getProperty(key);
        }
    }
}