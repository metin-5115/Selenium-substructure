package runners;

import factory.DriverFactory;
import factory.TargetFactory;
import factory.ThreadFactory;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import listener.ResultListener;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import utils.PropertiesReader;
import listener.DynamicParallelAlterer;

import java.io.File;
import java.io.IOException;

/**
 * TestRunner
 *
 * Purpose:
 *  - Integrates Cucumber with TestNG using TestNGCucumberRunner.
 *  - Controls test lifecycle: suite/class setup, per-method driver setup,
 *    scenario execution via DataProvider (parallel), and teardown.
 *
 * Key points:
 *  - @CucumberOptions: points to features, glue packages, tags, and Extent adapter plugin.
 *  - @Listeners(ResultListener.class): hooks in a TestNG listener for lifecycle events.
 *  - @DataProvider(parallel = true): enables parallel execution at scenario level.
 *  - @BeforeMethod: creates a WebDriver per test thread via TargetFactory and stores it in DriverFactory (ThreadLocal).
 *  - @AfterSuite: cleans up the screenshots folder after the entire suite.
 */

@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepDefinitions","hook"},
        // tags'ı buradan kaldırdık; CLI: -Dcucumber.filter.tags="..."
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
@Listeners({ResultListener.class, DynamicParallelAlterer.class})
public class TestRunner {

    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeSuite(alwaysRun = true)
    public void suiteStart(){
        System.setProperty("basefolder.name",
                "test-reports" + File.separator + PropertiesReader.getParameter("environment") + File.separator);

        // yalnızca bilgi amaçlı log — gerçek thread sayısı DynamicParallelAlterer tarafından set ediliyor
        String threads = System.getProperty("threads", "<dynamic-by-alterer>");
        String gridUrl = System.getProperty("gridUrl", "");
        String browser = System.getProperty("browser", "chrome");
        System.out.printf("[INFO] suite start | threads:%s | browser:%s | grid:%s%n",
                threads, browser, gridUrl.isEmpty() ? "LOCAL" : gridUrl);
    }

    @BeforeClass(alwaysRun = true)
    public void setUpCucumber() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "browser" })
    public void beforeMethod(@Optional("chrome") String browser){
        // Driver init Hook'ta yapılıyor; burada sadece chosen browser'ı publish ediyoruz
        System.setProperty("browser", browser);
        System.out.printf("[INFO] test method start | thread:%s | browser:%s%n",
                Thread.currentThread().getId(), browser);
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteEnd(){
        // screenshot klasörünü temizle (senin mevcut davranışın)
        String destination = new File(System.getProperty("user.dir")+"/screenshots").getAbsolutePath();
        if (new File(destination).exists()) {
            try { FileUtils.deleteDirectory(new File(destination)); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
        System.out.println("[INFO] suite end");
    }
}
