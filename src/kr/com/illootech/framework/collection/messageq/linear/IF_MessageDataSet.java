

package kr.com.illootech.framework.collection.messageq.linear;

import java.util.LinkedHashMap;

public interface IF_MessageDataSet
{
    boolean init();
    
    void release();
    
    boolean setMessageId(final String p0);
    
    String getMessageId();
    
    boolean setSourcePacket(final byte[] p0);
    
    byte[] getSourcePacket();
    
    boolean setHeader(final LinkedHashMap<String, byte[]> p0);
    
    boolean setPayload(final LinkedHashMap<String, byte[]> p0);
    
    LinkedHashMap<String, byte[]> getHeader();
    
    LinkedHashMap<String, byte[]> getPayload();
}
