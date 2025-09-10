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

@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepDefinitions","hook"},
        tags = "",
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
@Listeners(ResultListener.class)
public class TestRunner{
    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeSuite
    public void setUpSuite(){
        System.setProperty("basefolder.name","test-reports" + File.separator + PropertiesReader.getParameter("environment") + File.separator);
    }

    @BeforeClass(alwaysRun = true)
    public void setUpCucumber() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "browser"})
    public synchronized void setUpClass(String browser) {

        new DriverFactory().setDriver(new TargetFactory().createInstance(browser));
        ThreadFactory.setBrowserInfo(browser);
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
        //this code should work if you are going to check the hotel destination with the given parameter
//        HotelDestination.checkHotelDestination();
//        new WriteDataToExcel().writePageErrorsToExcel(paymentErrors, getDate(0,"dd-MM-yyyy HH-mm") +"-payment-errors");
//        new WriteDataToExcel().writePageErrorsToExcel(hotelListErrors, Helper.getDate(0,"dd-MM-yyyy HH-mm") +"-hotel-list-errors");
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite(){
        String destination = new File(System.getProperty("user.dir")+"/screenshots").getAbsolutePath();
        if (new File(destination).exists()) {
            try {
                FileUtils.deleteDirectory(new File(destination));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
