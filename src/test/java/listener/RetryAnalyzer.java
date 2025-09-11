package listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.PropertiesReader;

import java.io.File;

/**
 * RetryAnalyzer
 *
 * Purpose:
 *  - Implements retry logic for failed tests in TestNG.
 *  - If a test fails, it will be re-executed up to `maxTry` times.
 *  - `retryCount` is read from configuration.properties.
 *
 * Behavior:
 *  - If test fails and retry count not reached:
 *      - Increments retry counter
 *      - Marks test as SKIPPED (so TestNG will re-run it)
 *      - Returns true (retry will happen)
 *  - If max retry attempts reached:
 *      - Marks test as FAILURE
 *      - Returns false (no more retries)
 *  - If test passes:
 *      - Marks test as SUCCESS
 *
 * Example:
 *   configuration.properties → retryCount=2
 *   A failed test will run up to 2 more times before being marked as FAIL.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    // Current retry count (shared across tests)
    public static int count = 0;

    // Maximum retry attempts, read from configuration.properties
    public static int maxTry = Integer.parseInt(PropertiesReader.getParameter("retryCount"));

    /**
     * Decides whether the test should be retried.
     *
     * @param iTestResult the result of the test execution
     * @return true if retry should happen, false otherwise
     */
    @Override
    public boolean retry(ITestResult iTestResult) {
        if (iTestResult.getStatus() != ITestResult.SUCCESS) {
            if (count < maxTry) {
                count++;
                iTestResult.setStatus(ITestResult.SKIP); // mark as skipped for retry
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE); // max retries reached → fail
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS); // test passed → no retry
        }
        return false;
    }
}
