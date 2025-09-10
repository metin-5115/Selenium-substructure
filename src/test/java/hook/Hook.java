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



    @Before()
    public void startScenario(Scenario scenario){
//        ThreadFactory.setBrowserInfo("chrome");
//        new DriverFactory().setDriver(new TargetFactory().createInstance(ThreadFactory.getBrowserInfo()));

        System.out.println("=== Starting Scenario: " + scenario.getName() + " ===");
        if (!scenario.getSourceTagNames().isEmpty()) {
            System.out.println("Tags: " + scenario.getSourceTagNames());
        }
    }

    @After(order = 0)
    public void finishScenario(){
        if (DriverFactory.getDriver() != null)
            new DriverFactory().quit();

    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        if (scenario.getStatus().equals("SKIPPED")){
            System.out.println("scenario is skipped: "+scenario.getStatus().equals("SKIPPED"));
        }else{
            System.out.println("scenario is skipped: "+scenario.getStatus().equals("SKIPPED"));
        }
        if (scenario.isFailed() || scenario.getStatus().equals("SKIPPED")) {
            List<String> tagName = (List<String>) scenario.getSourceTagNames();
            String filePath = System.getProperty("user.dir")+File.separator+"screenshots";

        }
        new DriverFactory().quit();
    }


}
