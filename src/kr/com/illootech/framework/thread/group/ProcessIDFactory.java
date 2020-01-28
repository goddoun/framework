

package kr.com.illootech.framework.thread.group;

import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.file.log.Logger;

public class ProcessIDFactory
{
    private static final int limit = 100000;
    private static int processID;
    
    static {
        ProcessIDFactory.processID = -1;
    }
    
    public static synchronized int get(final String procname) {
        try {
            if (++ProcessIDFactory.processID >= 100000) {
                ProcessIDFactory.processID = -1;
            }
            return ++ProcessIDFactory.processID;
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][FRAMEWORK][Created processor's ID] - %3$s", procname, (ProcessIDFactory.processID > 0) ? "RUNNING" : "ERROR", (ProcessIDFactory.processID > 0) ? Integer.valueOf(ProcessIDFactory.processID) : "N/A", (ProcessIDFactory.processID > 0) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
}
