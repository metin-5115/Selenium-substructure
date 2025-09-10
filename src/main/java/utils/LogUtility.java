package utils;

import factory.ThreadFactory;
import org.apache.logging.log4j.ThreadContext;

import java.io.File;

public class LogUtility {

    public LogUtility(){
        String strFile = "test-logs"
                + File.separator + new BrowserUtility().getBrowserOs()
                + File.separator + PropertiesReader.getParameter("environment")
                + File.separator + ThreadFactory.getBrowserInfo()
                + File.separator + new BrowserUtility().getBrowserVersion()
                + File.separator + Thread.currentThread().getName();
        File logFile = new File(strFile);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        //route logs to separate file for each thread
        ThreadContext.put("ROUTINGKEY", strFile);
    }
}
