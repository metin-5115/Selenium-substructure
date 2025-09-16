package listener;

import utils.CpuSizer;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import java.util.List;
import java.util.Map;

public class DynamicParallelAlterer implements IAlterSuiteListener {
    @Override
    public void alter(List<XmlSuite> suites) {
        boolean ioBound = true; // Selenium için

        for (XmlSuite suite : suites) {
            // testsuite.xml parametrelerini System props’a geçir
            Map<String, String> p = suite.getParameters();
            copyParamToSys(p, "CPU_PERCENT");
            copyParamToSys(p, "IO_FACTOR");
            copyParamToSys(p, "MAX_THREADS");
            copyParamToSys(p, "BROWSER_LIMIT");

            // *** En başta terminale özet BAS ***
            CpuSizer.printBanner(ioBound);

            int finalThreads = CpuSizer.suggestThreads(ioBound);

            if (suite.getParallel() == XmlSuite.ParallelMode.NONE) {
                suite.setParallel(XmlSuite.ParallelMode.METHODS); // istersen CLASSES
            }
            suite.setThreadCount(finalThreads);
            suite.setDataProviderThreadCount(finalThreads);
        }
    }

    private static void copyParamToSys(Map<String,String> p, String key) {
        String v = p.get(key);
        if (v != null && !v.isEmpty()) System.setProperty(key, v);
    }
}