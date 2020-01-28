

package kr.com.illootech.framework.thread;

public interface IFRCThread
{
    boolean startup(final String p0);
    
    void shutdown();
    
    void delay(final long p0);
    
    boolean getLoopAllowFlag();
    
    String getProcname();
    
    int getProcId();
}
