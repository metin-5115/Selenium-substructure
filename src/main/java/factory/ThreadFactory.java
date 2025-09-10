package factory;

public class ThreadFactory {
    private static ThreadLocal <String> browserInfo = new ThreadLocal<>();
    private static ThreadLocal <String> scenarioInfo = new ThreadLocal<>();

    public static String getBrowserInfo() {
        return browserInfo.get();
    }
    public static void setBrowserInfo(String browserInfo1) {
        browserInfo.set(browserInfo1);
    }

    public static String getScenarioInfo() {
        return scenarioInfo.get();
    }

    public static void setScenarioInfo(String scenarioInfo1) {
        scenarioInfo.set(scenarioInfo1);
    }
}