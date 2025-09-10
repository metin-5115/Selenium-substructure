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

public class BasePage {
    protected WebDriver driver;
    protected JavascriptExecutor jsExec;

    protected final Logger log = LogManager.getLogger(BasePage.class);
    protected Duration waitSecond = Duration.ofSeconds(35);
    protected WebDriverWait wait;
    protected BasePage(WebDriver driver){
        this.driver = driver;
        System.setProperty("file.encoding", "UTF-8");
        wait = new WebDriverWait(driver, waitSecond);
        jsExec = (JavascriptExecutor) this.driver;
        new LogUtility();
        PageFactory.initElements(this.driver, this);
    }
    protected void waitForLoad() {
        try {
            new WebDriverWait(driver, waitSecond).until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    public void wait(int second){
        try {
            Thread.sleep(second*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitms(int msecond){
        try {
            Thread.sleep(msecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void navigate(){
        driver.get(PropertiesReader.getParameter(PropertiesReader.getParameter("environment")+".url"));
        driver.manage().window().maximize();
        log.info(Thread.currentThread().getName()+" : " + PropertiesReader.getParameter(PropertiesReader.getParameter("environment")+".url") + " adresi "+ ThreadFactory.getBrowserInfo() + " browser ile açıldı");
    }

    protected void navigateSpecific(String url){
        driver.get(url);
        driver.manage().window().maximize();
        log.info(Thread.currentThread().getName()+" : " + url + " adresi "+ ThreadFactory.getBrowserInfo() + " browser ile açıldı");
    }
    protected void navigateBack(){
        driver.navigate().back();
        log.info(Thread.currentThread().getName()+" bir önceki sayfaya dönüldü.");
    }
    protected void refresh(){
        driver.navigate().refresh();
        log.info(Thread.currentThread().getName()+" sayfa yenilendi.");
    }
    protected void waitVisibility(WebElement element) {
        wait = new WebDriverWait(driver, waitSecond);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        }catch (org.openqa.selenium.TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }catch (org.openqa.selenium.StaleElementReferenceException e){
            wait(2);
            wait.until(ExpectedConditions.visibilityOf(element));
        }
    }

    protected void waitVisibility(By by) {
        try{
            wait = new WebDriverWait(driver, waitSecond);
            wait.until(visibilityOfElementLocated(by));
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    protected void waitVisibilityWithSecond(By by, int second) {
        try{
            wait = new WebDriverWait(driver, Duration.ofSeconds(second));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        }catch (TimeoutException e){
            throw new ScenarioInfoException("Element visibility timed out: " + e.getMessage());
        }
    }

    public WebElement waitPresence(By by) {
        try{
            wait = new WebDriverWait(driver, waitSecond);
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }


    protected WebElement waitClickable(WebElement element) {
        try {
            wait = new WebDriverWait(driver, waitSecond);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected WebElement find(By by) {
        try{
//            waitForLoad();
            waitPresence(by);
            WebElement element = driver.findElement(by);
            log.info(Thread.currentThread().getName()+" : " + by+" elementi bulundu.");
            return element;
        }catch (NoSuchElementException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    public List<WebElement> finds(By by) {
        try{
//            waitForLoad();
            waitVisibility(by);
            List<WebElement> ListElement = driver.findElements(by);
            log.info(Thread.currentThread().getName()+" : "+ by +" elementleri bulundu.");
            return ListElement;
        }catch (NoSuchElementException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    protected void sendKeys(WebElement element,String text) {
        try{
//            waitForLoad();
//			waitVisibility(element);

            element.sendKeys(text);
            log.info(Thread.currentThread().getName()+" : "+element+" elementine "+text+" yazılıyor.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected void sendKeys(WebElement element, Keys... keys) {
        try {
            // Öğeye odaklan (gerekli olabilir)
            element.click();

            // Gönderilen Keys parametrelerine sırayla bas
            for (Keys key : keys) {
                element.sendKeys(key);
            }
        } catch (TimeoutException e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    protected void sendKeys(By by,String text) {
        try{
//            waitForLoad();
//			waitVisibility(element);
            find(by).sendKeys(text);
            log.info(Thread.currentThread().getName()+" : "+by+" elementine "+text+" yazılıyor.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    protected void sendKeysClear(WebElement element) {
        try{
//            waitForLoad();
            element.clear();
            log.info(Thread.currentThread().getName()+" : "+element+" elementindeki text temizleniyor.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected void sendKeysClear(By by) {
        try{
//            waitForLoad();
            find(by).clear();
            log.info(Thread.currentThread().getName()+" : "+by+" elementindeki text temizleniyor.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected void click(WebElement element) {
        try{
//            waitForLoad();
            waitClickable(element);

            element.click();
            log.info(Thread.currentThread().getName()+" : "+element+" elementine tıklandı.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected void click(By by) {
        try{
//            waitForLoad();
            waitClickable(find(by));
            find(by).click();
            log.info(Thread.currentThread().getName()+" : "+by+" elementine tıklandı.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected void clickJS(WebElement element) {
        try{
            waitVisibility(element);
            jsExec.executeScript("arguments[0].click();", element);
            log.info(Thread.currentThread().getName()+" : "+element+" elementine JS ile tıklandı.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected void clickJS(By by) {
        try{
            waitVisibility(by);
            WebElement element =driver.findElement(by);
            jsExec.executeScript("arguments[0].click();", element);
            log.info(Thread.currentThread().getName()+" : "+by+" elementine JS ile tıklandı.");
        }catch (TimeoutException e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    protected String getElementText(WebElement element){
        waitVisibility(element);
        return element.getText();
    }

    protected String getElementText(By by){
        return find(by).getText();
    }
    protected List<String> getAllElementText(By by){
        List<String> list2 = new ArrayList<>();
        if (isElementExist(by)){
            List<WebElement> list = driver.findElements(by);
            for (WebElement element : list){
                list2.add(element.getText());
            }
        }
        return list2;
    }

    public boolean isElementExist(WebElement element) {
        try {
            log.info(Thread.currentThread().getName()+" : "+ element + " elementi var.");
            return element.isDisplayed();
        } catch(NoSuchElementException e) {
            log.info(Thread.currentThread().getName()+" : "+ element + " elementi yok.");
            return false;
        }
    }
    public boolean isElementExist(By by) {
        if (driver.findElements(by).size() > 0) {
            log.info(Thread.currentThread().getName()+" : "+by + " elementi var.");
            return true;
        }else {
            log.info(Thread.currentThread().getName()+" : "+by+" elementi yok.");
            return false;
        }
    }
}
