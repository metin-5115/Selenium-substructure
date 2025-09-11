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
        features = "src/test/java/features",                          // Feature file root directory
        glue = {"stepDefinitions","hook"},                             // Step defs + hooks packages
        tags = "",                                                     // Optional: filter scenarios by tags (e.g., "@reservation")
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"} // Extent Reports adapter
)
@Listeners(ResultListener.class)                                       // Attach custom TestNG listener
public class TestRunner{
    private TestNGCucumberRunner testNGCucumberRunner;                 // Bridges Cucumber with TestNG

    /**
     * Runs once before the entire suite.
     * Sets the base folder for Extent report output depending on environment.
     */
    @BeforeSuite
    public void setUpSuite(){
        System.setProperty(
                "basefolder.name",
                "test-reports" + File.separator + PropertiesReader.getParameter("environment") + File.separator
        );
    }

    /**
     * Runs once before any test methods in this class.
     * Initializes the TestNGCucumberRunner with this test class context.
     */
    @BeforeClass(alwaysRun = true)
    public void setUpCucumber() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    /**
     * Runs before each test method (each scenario execution).
     * Creates a WebDriver instance for the given browser and stores it in a ThreadLocal (DriverFactory).
     * Also stores the browser name in ThreadFactory for logging/routing purposes.
     *
     * @param browser passed from TestNG (e.g., via testng.xml or -Dbrowser=chrome)
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({ "browser"})
    public synchronized void setUpClass(String browser) {
        new DriverFactory().setDriver(new TargetFactory().createInstance(browser)); // Create and bind driver to current thread
        ThreadFactory.setBrowserInfo(browser);                                      // Save browser info for logs
    }

    /**
     * Executes a single Cucumber scenario provided by the DataProvider.
     * Each row from scenarios() corresponds to one scenario run.
     */
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    /**
     * Supplies scenarios to the test method.
     * parallel = true → allows multiple scenarios to run concurrently.
     */
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    /**
     * Runs once after all test methods in this class.
     * Finalizes/flushes the Cucumber-TestNG runner.
     * (Commented examples below show where post-processing/export logic could be placed.)
     */
    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
//        HotelDestination.checkHotelDestination();
//        new WriteDataToExcel().writePageErrorsToExcel(paymentErrors, getDate(0,"dd-MM-yyyy HH-mm") +"-payment-errors");
//        new WriteDataToExcel().writePageErrorsToExcel(hotelListErrors, Helper.getDate(0,"dd-MM-yyyy HH-mm") +"-hotel-list-errors");
    }

    /**
     * Runs once after the entire suite finishes.
     * Deletes the screenshots directory (if exists) to keep workspace clean between runs.
     */
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite(){
        String destination = new File(System.getProperty("user.dir")+"/screenshots").getAbsolutePath();
        if (new File(destination).exists()) {
            try {
                FileUtils.deleteDirectory(new File(destination));      // Remove screenshots directory
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
