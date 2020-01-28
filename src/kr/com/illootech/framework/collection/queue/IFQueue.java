

package kr.com.illootech.framework.collection.queue;

public interface IFQueue
{
    boolean init(final String p0);
    
    void release();
    
    boolean push(final Object p0);
    
    Object poll();
    
    long getLimit();
    
    long getFreeSpace();
    
    void clear();
    
    String getQueueName();
    
    long getFrontPos();
    
    long getRearPos();
}
