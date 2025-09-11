package hook;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Hook {



    /**
     * Executed before each scenario starts.
     * - Prints scenario name to the console.
     * - Prints scenario tags if available.
     * - (Optional) You can also initialize WebDriver here
     *   by uncommenting the ThreadFactory/DriverFactory lines.
     */
    @Before()
    public void startScenario(Scenario scenario){
        // ThreadFactory.setBrowserInfo("chrome");
        // new DriverFactory().setDriver(new TargetFactory().createInstance(ThreadFactory.getBrowserInfo()));

        System.out.println("=== Starting Scenario: " + scenario.getName() + " ===");
        if (!scenario.getSourceTagNames().isEmpty()) {
            System.out.println("Tags: " + scenario.getSourceTagNames());
        }
    }

    /**
     * Executed after each scenario (order = 0).
     * - Quits WebDriver if it exists.
     * - Ensures driver cleanup happens at the end of a scenario.
     */
    @After(order = 0)
    public void finishScenario(){
        if (DriverFactory.getDriver() != null)
            new DriverFactory().quit();
    }

    /**
     * Executed after each scenario (order = 1).
     * - Prints scenario status (skipped or not).
     * - If scenario failed or skipped, prepares a path for screenshots.
     * - Quits WebDriver again for safety.
     *
     * Note:
     *   There is a duplicated quit() call here
     *   (in finishScenario and again in tearDown).
     *   This is safe but redundant.
     */
    @After(order = 1)
    public void tearDown(Scenario scenario) {
        if (scenario.getStatus().equals("SKIPPED")){
            System.out.println("scenario is skipped: " + scenario.getStatus().equals("SKIPPED"));
        } else {
            System.out.println("scenario is skipped: " + scenario.getStatus().equals("SKIPPED"));
        }

        if (scenario.isFailed() || scenario.getStatus().equals("SKIPPED")) {
            List<String> tagName = (List<String>) scenario.getSourceTagNames();
            String filePath = System.getProperty("user.dir") + File.separator + "screenshots";
            // Screenshot handling can be added here
        }

        new DriverFactory().quit();
    }


}
