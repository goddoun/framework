

package kr.com.illootech.framework.collection.messageq.linear.object.dataset.map;

import kr.com.illootech.framework.file.log.Logger;
import java.util.HashMap;
import kr.com.illootech.framework.collection.messageq.linear.object.dataset.AbsMessageQueueDataSet;

public class MessageQueueHMapSet extends AbsMessageQueueDataSet
{
    private HashMap<String, byte[]> headerDataMap;
    private HashMap<String, byte[]> bodyDataMap;
    
    public MessageQueueHMapSet(final long timeout, final String sourceMessageQName) {
        super(timeout, sourceMessageQName);
        this.headerDataMap = null;
        this.bodyDataMap = null;
        this.headerDataMap = new HashMap<String, byte[]>();
        this.bodyDataMap = new HashMap<String, byte[]>();
    }
    
    public void setBody(final HashMap<String, byte[]> datamap) {
        this.bodyDataMap = datamap;
    }
    
    @Override
    public void setBodyElement(final byte[] data) {
    }
    
    @Override
    public void setBodyElement(final String key, final byte[] data) {
        this.bodyDataMap.put(key, data);
    }
    
    public HashMap<String, byte[]> getBody() {
        return this.bodyDataMap;
    }
    
    @Override
    public byte[] getBodyElement() {
        return null;
    }
    
    @Override
    public byte[] getBodyElement(final String key) {
        return this.bodyDataMap.get(key);
    }
    
    public void setHeader(final HashMap<String, byte[]> datamap) {
        this.headerDataMap = datamap;
    }
    
    @Override
    public void setHeaderElement(final byte[] data) {
    }
    
    @Override
    public void setHeaderElement(final String key, final byte[] data) {
        this.headerDataMap.put(key, data);
    }
    
    public HashMap<String, byte[]> getHeader() {
        return this.headerDataMap;
    }
    
    @Override
    public byte[] getHeaderElement() {
        return null;
    }
    
    @Override
    public byte[] getHeaderElement(final String key) {
        return this.headerDataMap.get(key);
    }
    
    @Override
    public void clear() {
        try {
            super.clear();
            if (this.headerDataMap != null) {
                this.headerDataMap.clear();
            }
            if (this.bodyDataMap != null) {
                this.bodyDataMap.clear();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.headerDataMap = null;
            this.bodyDataMap = null;
        }
    }
}
