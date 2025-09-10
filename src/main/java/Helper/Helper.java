package Helper;

import org.openqa.selenium.WebDriver;
import base.BasePage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Helper extends BasePage {

    public Helper(WebDriver driver) {
        super(driver);
    }

    public static String getDate(int nextDay, String format){
        String date = LocalDateTime.now().plusDays(nextDay).format(DateTimeFormatter.ofPattern(format, new Locale("tr")));
        return date;
    }
}