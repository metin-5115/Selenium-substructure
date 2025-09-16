package utils;

public final class ThreadSizer {
    private ThreadSizer() {}

    public static int suggestThreads(boolean cpuBoundDefault) {
        int cores = Runtime.getRuntime().availableProcessors();

        String sys = System.getProperty("TEST_THREADS");
        String env = System.getenv("TEST_THREADS");
        int override = parsePositiveInt(sys != null ? sys : env);
        if (override > 0) return override;

        int factor = cpuBoundDefault ? 1 : 3; // Selenium/I-O ağırlıklı testler için ~3×
        int threads = Math.max(1, cores * factor);

        int max = parsePositiveInt(System.getProperty("TEST_THREADS_MAX", "0"));
        if (max > 0) threads = Math.min(threads, max);
        return threads;
    }

    private static int parsePositiveInt(String s) {
        try { int v = Integer.parseInt(s); return v > 0 ? v : 0; }
        catch (Exception ignore) { return 0; }
    }
}
