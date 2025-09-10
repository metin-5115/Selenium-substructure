package listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.PropertiesReader;

import java.io.File;

public class RetryAnalyzer implements IRetryAnalyzer {

    public static int count = 0;
    public static int maxTry = Integer.parseInt(PropertiesReader.getParameter("retryCount"));

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (iTestResult.getStatus() != ITestResult.SUCCESS) {
            if (count < maxTry) {
                count++;
                iTestResult.setStatus(ITestResult.SKIP);
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);//If maxCount reached,test marked as failed

            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
        }
        return false;
    }

}
