package utils;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public final class CpuSizer {
    private CpuSizer(){}

    // JVM'in gördüğü mantıksal çekirdek (cgroup vs. yoksa gerçek mantıksal core)
    public static int logicalCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    private static int getIntProp(String key, int def) {
        try { return Integer.parseInt(System.getProperty(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    // ---- Ayarlar (testsuite.xml parametreleriyle beslenecek) ----
    // Varsayılanı %30
    public static int cpuPercent()   { return clamp01to100(getIntProp("CPU_PERCENT", 30)); }
    // Selenium I/O ağırlığı için 2 makul (2..3 arası)
    public static int ioFactor()     { return clamp( getIntProp("IO_FACTOR", 2), 1, 4 ); }
    // Sert üst sınır (0 = sınırsız)
    public static int maxThreads()   { return Math.max(0, getIntProp("MAX_THREADS", 0)); }
    // Aynı anda açık tarayıcı tavanı (0 = sınırsız)
    public static int browserLimit() { return Math.max(0, getIntProp("BROWSER_LIMIT", 0)); }

    public static int suggestThreads(boolean ioBound) {
        int cores  = logicalCores();
        int base   = Math.max(1, (cores * cpuPercent()) / 100);        // %30 → cores*0.30
        int factor = ioBound ? ioFactor() : 1;                          // I/O → çarpan uygula
        int t      = Math.max(1, base * factor);

        int max = maxThreads();
        if (max > 0) t = Math.min(t, max);
        int bl = browserLimit();
        if (bl > 0) t = Math.min(t, bl);

        return t;
    }

    // --- Bilgi çıktısı: CPU load ölç, hesapları yazdır ---
    public static void printBanner(boolean ioBound) {
        int cores   = logicalCores();
        int percent = cpuPercent();
        int factor  = ioBound ? ioFactor() : 1;

        double[] loads = measureLoadsMillis(1000); // ~1 sn örnekleme
        double sysLoadPct   = loads[0] >= 0 ? loads[0] * 100 : -1;
        double procLoadPct  = loads[1] >= 0 ? loads[1] * 100 : -1;

        int base = Math.max(1, (cores * percent) / 100);
        int suggested = Math.max(1, base * factor);
        int finalThreads = Math.max(1, Math.min(
                suggested,
                capNonZero(Math.min(maxThreadsOrInf(), browserLimitOrInf()))
        ));

        System.out.println("========== CPU / Parallel Plan ==========");
        System.out.println("CPU (logical cores): " + cores);
        System.out.println(String.format("Current CPU Load: system=%.1f%%, jvm=%.1f%%",
                sysLoadPct, procLoadPct));
        System.out.println("Config: CPU_PERCENT=" + percent + "%, IO_FACTOR=" + factor
                + ", MAX_THREADS=" + (maxThreads() > 0 ? maxThreads() : "∞")
                + ", BROWSER_LIMIT=" + (browserLimit() > 0 ? browserLimit() : "∞"));
        System.out.println("Calc: base=cores*percent = " + cores + "*" + percent + "% ≈ " + base);
        System.out.println("      suggested=base*IO_FACTOR = " + base + "*" + factor + " = " + suggested);
        System.out.println("FINAL parallel threads (after caps) = " + finalThreads);
        System.out.println("=========================================");
    }

    // --- helpers ---
    private static int clamp01to100(int v){ return clamp(v,1,100); }
    private static int clamp(int v,int lo,int hi){ return Math.max(lo, Math.min(hi, v)); }
    private static int capNonZero(int v){ return v <= 0 ? Integer.MAX_VALUE : v; }
    private static int maxThreadsOrInf(){ return maxThreads() > 0 ? maxThreads() : Integer.MAX_VALUE; }
    private static int browserLimitOrInf(){ return browserLimit() > 0 ? browserLimit() : Integer.MAX_VALUE; }

    private static OperatingSystemMXBean osBean() {
        try { return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean(); }
        catch (Throwable t){ return null; }
    }

    // 0..1 aralığında ortalama system & process load döndür (yoksa -1)
    public static double[] measureLoadsMillis(long ms) {
        OperatingSystemMXBean os = osBean();
        if (os == null) return new double[]{-1,-1};
        int steps = (int)Math.max(1, ms / 100);
        double sys = 0, proc = 0; int n = 0;
        for (int i=0;i<steps;i++){
            double s = safe(os.getSystemCpuLoad());
            double p = safe(os.getProcessCpuLoad());
            if (s >= 0) sys += s;
            if (p >= 0) proc += p;
            n++;
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        return new double[]{ n>0 ? sys/n : -1, n>0 ? proc/n : -1 };
    }
    private static double safe(double v){ return (v < 0 || v > 1) ? 0 : v; }
}