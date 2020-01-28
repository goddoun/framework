

package kr.com.illootech.framework.collection.messageq.linear;

public interface IF_MessageQueue
{
    boolean init(final String p0);
    
    boolean put(final IF_MessageDataSet p0);
    
    IF_MessageDataSet poll();
    
    String getQName();
    
    int qSize();
    
    void release();
}
