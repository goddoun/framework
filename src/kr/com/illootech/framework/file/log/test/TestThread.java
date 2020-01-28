

package kr.com.illootech.framework.file.log.test;


import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.thread.RCThread;

public class TestThread extends RCThread
{
    private int id;
    
    public TestThread(final String logname, final int id) {
        this.id = -1;
        LoggerElements.defaultSystemLogFileName = logname;
        this.id = id;
    }
    
    @Override
    public void run() {
        try {
            while (this.getLoopAllowFlag()) {
                final long startTime = System.currentTimeMillis();
                Logger.sysInfo(String.format("TEST.%1$04d", this.id), 1);
                System.out.println(String.format("TEST.%1$04d - %2$04d msec", this.id, System.currentTimeMillis() - startTime));
                this.delay(10L);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.shutdown();
        }
    }
}
