

package kr.com.illootech.framework.thread;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;

public class RCThread extends Thread implements IFRCThread
{
    private boolean loopAllow;
    
    public RCThread() {
        this.loopAllow = false;
    }
    
    @Override
    public boolean startup(final String name) {
        boolean result = false;
        try {
            this.loopAllow = true;
            this.setName(name);
            this.start();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][FRAMEWORK][Startup] - %3$s", this.getName(), result ? "RUNNING" : "ERROR", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL1);
        }
    }
    
    @Override
    public void shutdown() {
        this.loopAllow = false;
        this.interrupt();
        try {
            this.join();
        }
        catch (InterruptedException ex) {}
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][FRAMEWORK][Shutdown] - SUCCESS", this.getName(), "RUNNING"), LoggerElements.LOG_LEVEL1);
        }
        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][FRAMEWORK][Shutdown] - SUCCESS", this.getName(), "RUNNING"), LoggerElements.LOG_LEVEL1);
    }
    
    @Override
    public void delay(final long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) {}
        catch (Exception ex2) {}
    }
    
    @Override
    public boolean getLoopAllowFlag() {
        return this.loopAllow;
    }
    
    @Override
    public String getProcname() {
        return this.getName();
    }
    
    @Override
    public int getProcId() {
        return -1;
    }
}
