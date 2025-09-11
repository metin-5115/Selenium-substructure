package factory;

/**
 * ThreadFactory
 *
 * Purpose:
 *  - Stores thread-specific metadata for parallel test execution.
 *  - Uses ThreadLocal to keep info isolated between test threads.
 *  - Currently tracks:
 *      1. browserInfo   → which browser is used in this thread
 *      2. scenarioInfo  → which scenario (Cucumber/TestNG) is running
 *
 * Why ThreadLocal?
 *  - In parallel test execution, multiple threads may run different browsers
 *    and different scenarios at the same time.
 *  - ThreadLocal ensures each thread sees only its own data.
 *
 * Usage Example:
 *   ThreadFactory.setBrowserInfo("chrome");
 *   ThreadFactory.setScenarioInfo("reservation_scenario_01");
 *   ...
 *   String currentBrowser = ThreadFactory.getBrowserInfo();
 *   String currentScenario = ThreadFactory.getScenarioInfo();
 */
public class ThreadFactory {

    // Holds the browser info for the current test thread
    private static ThreadLocal<String> browserInfo = new ThreadLocal<>();

    // Holds the scenario info for the current test thread
    private static ThreadLocal<String> scenarioInfo = new ThreadLocal<>();

    /**
     * Returns the browser info for the current thread.
     */
    public static String getBrowserInfo() {
        return browserInfo.get();
    }

    /**
     * Sets the browser info for the current thread.
     */
    public static void setBrowserInfo(String browserInfo1) {
        browserInfo.set(browserInfo1);
    }

    /**
     * Returns the scenario info for the current thread.
     */
    public static String getScenarioInfo() {
        return scenarioInfo.get();
    }

    /**
     * Sets the scenario info for the current thread.
     */
    public static void setScenarioInfo(String scenarioInfo1) {
        scenarioInfo.set(scenarioInfo1);
    }
}