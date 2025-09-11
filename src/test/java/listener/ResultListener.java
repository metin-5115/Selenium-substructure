package listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
/**
 * ResultListener
 *
 * Custom TestNG listener implementing ITestListener.
 *
 * Purpose:
 *  - Provides hooks for different stages of a test lifecycle.
 *  - Currently, each method just calls the default (super) implementation.
 *  - Can be extended to add custom logging, reporting, screenshots, or Jira integration.
 *
 * Common use cases:
 *  - onTestFailure: take a screenshot and attach it to report.
 *  - onTestSuccess: log test success to external system.
 *  - onStart/onFinish: initialize or close reporting tools (ExtentReports, Allure, etc.).
 */
public class ResultListener implements ITestListener {

    /**
     * Called when a test method starts.
     */
    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
    }

    /**
     * Called when a test method passes successfully.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
    }

    /**
     * Called when a test method fails.
     * Ideal place to add:
     *   - Screenshot capture
     *   - Logging failure details
     *   - Reporting to external system
     */
    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
    }

    /**
     * Called when a test fails but is within the success percentage
     * configured in TestNG (rarely used).
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    /**
     * Called when a test fails due to a timeout.
     */
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    /**
     * Called before any test methods of the current test class are invoked.
     * Useful for initializing reports/resources.
     */
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    /**
     * Called after all test methods of the current test class have run.
     * Useful for flushing reports or releasing resources.
     */
    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }
}