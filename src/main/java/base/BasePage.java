package base;
import Helper.Helper;
import exception.ScenarioInfoException;
import factory.ThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BrowserUtility;
import utils.LogUtility;
import utils.PropertiesReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * BasePage
 *
 * Shared Selenium helpers for all Page Objects:
 * - Navigation (open/refresh/back)
 * - Explicit waits (visibility/presence/clickable)
 * - Safe find/click/sendKeys utilities
 * - Small DOM helpers (get text, existence checks)
 *
 * Usage: extend this class in your page objects and pass the current WebDriver
 * from your test setup (e.g., DriverFactory.getDriver()).
 */
public class BasePage {

    // --- Web context ---
    protected WebDriver driver;
    protected JavascriptExecutor jsExec;

    // --- Logging & waiting ---
    protected final Logger log = LogManager.getLogger(BasePage.class);
    protected Duration waitSecond = Duration.ofSeconds(35);
    protected WebDriverWait wait;

    /**
     * Initializes common utilities and page elements.
     * - Sets UTF-8 encoding for logs/output.
     * - Creates a default WebDriverWait with 35s timeout.
     * - Initializes PageFactory elements.
     * - Initializes per-thread logging routing (LogUtility).
     */
    protected BasePage(WebDriver driver){
        this.driver = driver;
        System.setProperty("file.encoding", "UTF-8");
        wait = new WebDriverWait(driver, waitSecond);
        jsExec = (JavascriptExecutor) this.driver;
        new LogUtility(); // route logs by thread/browser/env
        PageFactory.initElements(this.driver, this);
    }

    /**
     * Waits until document.readyState == "complete".
     * Throws ScenarioInfoException on timeout.
     */
    protected void waitForLoad() {
        try {
            new WebDriverWait(driver, waitSecond).until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Hard sleep in seconds (use explicit waits where possible).
     */
    public void wait(int second){
        try {
            Thread.sleep(second*1000L);
        } catch (InterruptedException e) {
            e.printStackTrace(); // consider: restore interrupt flag
        }
    }

    /**
     * Hard sleep in milliseconds (use explicit waits where possible).
     */
    public void waitms(int msecond){
        try {
            Thread.sleep(msecond);
        } catch (InterruptedException e) {
            e.printStackTrace(); // consider: restore interrupt flag
        }
    }

    /**
     * Navigates to the environment base URL from configuration:
     * key: {environment}.url  (e.g., dev.url, qa.url)
     * Maximizes the window and logs the action.
     */
    protected void navigate(){
        String env = PropertiesReader.getParameter("environment");
        String url = PropertiesReader.getParameter(env + ".url");
        driver.get(url);
        driver.manage().window().maximize();
        log.info(Thread.currentThread().getName()+" : " + url + " opened with " + ThreadFactory.getBrowserInfo());
    }

    /**
     * Navigates to a specific URL, maximizes window, logs the action.
     */
    protected void navigateSpecific(String url){
        driver.get(url);
        driver.manage().window().maximize();
        log.info(Thread.currentThread().getName()+" : " + url + " opened with " + ThreadFactory.getBrowserInfo());
    }

    /**
     * Browser back navigation with log.
     */
    protected void navigateBack(){
        driver.navigate().back();
        log.info(Thread.currentThread().getName()+" navigated back.");
    }

    /**
     * Page refresh with log.
     */
    protected void refresh(){
        driver.navigate().refresh();
        log.info(Thread.currentThread().getName()+" page refreshed.");
    }

    /**
     * Waits until an element (WebElement) is visible.
     * Retries once on StaleElementReferenceException.
     */
    protected void waitVisibility(WebElement element) {
        wait = new WebDriverWait(driver, waitSecond);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (org.openqa.selenium.TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        } catch (org.openqa.selenium.StaleElementReferenceException e){
            wait(2);
            wait.until(ExpectedConditions.visibilityOf(element));
        }
    }

    /**
     * Waits until an element located by By is visible.
     */
    protected void waitVisibility(By by) {
        try{
            wait = new WebDriverWait(driver, waitSecond);
            wait.until(visibilityOfElementLocated(by));
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Waits visibility with a custom timeout (in seconds).
     */
    protected void waitVisibilityWithSecond(By by, int second) {
        try{
            wait = new WebDriverWait(driver, Duration.ofSeconds(second));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e){
            throw new ScenarioInfoException("Element visibility timed out: " + e.getMessage());
        }
    }

    /**
     * Waits for presence of element located by By and returns it.
     */
    public WebElement waitPresence(By by) {
        try{
            wait = new WebDriverWait(driver, waitSecond);
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Waits until a WebElement is clickable and returns it.
     */
    protected WebElement waitClickable(WebElement element) {
        try {
            wait = new WebDriverWait(driver, waitSecond);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Safe find – ensures presence first, then finds the element. Logs success.
     */
    protected WebElement find(By by) {
        try{
            // waitForLoad(); // optional: enable if needed
            waitPresence(by);
            WebElement element = driver.findElement(by);
            log.info(Thread.currentThread().getName()+" : " + by+" element found.");
            return element;
        } catch (NoSuchElementException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Finds multiple elements after ensuring visibility of at least one. Logs success.
     */
    public List<WebElement> finds(By by) {
        try{
            // waitForLoad(); // optional: enable if needed
            waitVisibility(by);
            List<WebElement> listElement = driver.findElements(by);
            log.info(Thread.currentThread().getName()+" : "+ by +" elements found.");
            return listElement;
        } catch (NoSuchElementException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Sends text to a WebElement. Consider waiting for visibility before typing.
     */
    protected void sendKeys(WebElement element, String text) {
        try{
            // waitForLoad(); // optional
            // waitVisibility(element); // optional
            element.sendKeys(text);
            log.info(Thread.currentThread().getName()+" : typing '"+text+"' into "+element);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Sends Keys to a WebElement in sequence (e.g., TAB, ENTER).
     * Clicks first to focus the element.
     */
    protected void sendKeys(WebElement element, Keys... keys) {
        try {
            element.click(); // ensure focus
            for (Keys key : keys) {
                element.sendKeys(key);
            }
        } catch (TimeoutException e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Finds an element by locator and sends text to it.
     */
    protected void sendKeys(By by, String text) {
        try{
            // waitForLoad(); // optional
            find(by).sendKeys(text);
            log.info(Thread.currentThread().getName()+" : typing '"+text+"' into "+by);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Clears a WebElement's text (clear()) with logging.
     */
    protected void sendKeysClear(WebElement element) {
        try{
            // waitForLoad(); // optional
            element.clear();
            log.info(Thread.currentThread().getName()+" : clearing text in "+element);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Clears text for an element located by By (clear()) with logging.
     */
    protected void sendKeysClear(By by) {
        try{
            // waitForLoad(); // optional
            find(by).clear();
            log.info(Thread.currentThread().getName()+" : clearing text in "+by);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Clicks a WebElement after ensuring it is clickable. Logs the action.
     */
    protected void click(WebElement element) {
        try{
            // waitForLoad(); // optional
            waitClickable(element);
            element.click();
            log.info(Thread.currentThread().getName()+" : clicked "+element);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Finds and clicks an element located by By after waiting for clickability. Logs the action.
     */
    protected void click(By by) {
        try{
            // waitForLoad(); // optional
            waitClickable(find(by));
            find(by).click();
            log.info(Thread.currentThread().getName()+" : clicked "+by);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * JavaScript click on a WebElement (useful when normal click is blocked/overlayed).
     */
    protected void clickJS(WebElement element) {
        try{
            waitVisibility(element);
            jsExec.executeScript("arguments[0].click();", element);
            log.info(Thread.currentThread().getName()+" : JS-clicked "+element);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * JavaScript click on an element located by By.
     */
    protected void clickJS(By by) {
        try{
            waitVisibility(by);
            WebElement element = driver.findElement(by);
            jsExec.executeScript("arguments[0].click();", element);
            log.info(Thread.currentThread().getName()+" : JS-clicked "+by);
        } catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Returns visible text of the given WebElement (waits visibility first).
     */
    protected String getElementText(WebElement element){
        waitVisibility(element);
        return element.getText();
    }

    /**
     * Returns visible text of element located by By (finds it first).
     */
    protected String getElementText(By by){
        return find(by).getText();
    }

    /**
     * Returns the text of all elements matched by the locator (if any exist).
     */
    protected List<String> getAllElementText(By by){
        List<String> texts = new ArrayList<>();
        if (isElementExist(by)){
            List<WebElement> list = driver.findElements(by);
            for (WebElement element : list){
                texts.add(element.getText());
            }
        }
        return texts;
    }

    /**
     * Checks if a WebElement is displayed. Logs result.
     */
    public boolean isElementExist(WebElement element) {
        try {
            boolean displayed = element.isDisplayed();
            log.info(Thread.currentThread().getName()+" : element exists: " + displayed + " -> " + element);
            return displayed;
        } catch(NoSuchElementException e) {
            log.info(Thread.currentThread().getName()+" : element does not exist: " + element);
            return false;
        }
    }

    /**
     * Checks if any element matching the locator exists (size > 0). Logs result.
     */
    public boolean isElementExist(By by) {
        if (driver.findElements(by).size() > 0) {
            log.info(Thread.currentThread().getName()+" : "+by + " exists.");
            return true;
        } else {
            log.info(Thread.currentThread().getName()+" : "+by+" does not exist.");
            return false;
        }
    }
}
