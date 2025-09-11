package Helper;

import org.openqa.selenium.WebDriver;
import base.BasePage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.openqa.selenium.WebDriver;

/**
 * Helper
 *
 * Utility class extending BasePage for common reusable functions.
 * Currently provides:
 *   - getDate(): format current date/time (with optional day offset).
 *
 * Extends BasePage so it can also access all Selenium helpers if needed.
 */
public class Helper extends BasePage {

    /**
     * Constructor passing the WebDriver to BasePage.
     *
     * @param driver active WebDriver instance
     */
    public Helper(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns a formatted date string.
     *
     * @param nextDay number of days to add (e.g., 0 = today, 1 = tomorrow, -1 = yesterday)
     * @param format  date format pattern (e.g., "dd-MM-yyyy", "yyyy/MM/dd HH:mm")
     * @return formatted date string in Turkish locale
     *
     * Example:
     *   getDate(0, "dd-MM-yyyy")  -> "11-09-2025"
     *   getDate(1, "EEEE, dd MMMM yyyy") -> "Perşembe, 12 Eylül 2025"
     */
    public static String getDate(int nextDay, String format){
        return LocalDateTime.now()
                .plusDays(nextDay)
                .format(DateTimeFormatter.ofPattern(format, new Locale("your language")));
    }
}