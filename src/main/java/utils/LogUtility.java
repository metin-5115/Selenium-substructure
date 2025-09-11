package utils;

import factory.ThreadFactory;
import org.apache.logging.log4j.ThreadContext;

import java.io.File;


/**
 * LogUtility
 *
 * Purpose:
 *  - Dynamically builds a log file path per test execution.
 *  - Creates directories for logs based on:
 *      OS → environment → browser → version → thread name
 *  - Routes Log4j logs to this thread-specific path
 *    using ThreadContext's "ROUTINGKEY".
 *
 * Example folder structure:
 *   test-logs/
 *     windows/
 *       qa/
 *         chrome/
 *           114.0/
 *             TestNG-PoolService-0/
 *               product.log
 */
public class LogUtility {

    public LogUtility() {
        String strFile = "test-logs"
                + File.separator + new BrowserUtility().getBrowserOs()
                + File.separator + PropertiesReader.getParameter("environment")
                + File.separator + ThreadFactory.getBrowserInfo()
                + File.separator + new BrowserUtility().getBrowserVersion()
                + File.separator + Thread.currentThread().getName();

        File logFile = new File(strFile);
        if (!logFile.exists()) {
            logFile.mkdirs(); // create nested folders if missing
        }

        // Route logs to separate file for each thread via RoutingAppender (log4j2.xml)
        ThreadContext.put("ROUTINGKEY", strFile);
    }
}
