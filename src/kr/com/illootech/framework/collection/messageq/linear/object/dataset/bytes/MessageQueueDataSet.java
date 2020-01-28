

package kr.com.illootech.framework.collection.messageq.linear.object.dataset.bytes;

import kr.com.illootech.framework.collection.messageq.linear.object.dataset.AbsMessageQueueDataSet;

public class MessageQueueDataSet extends AbsMessageQueueDataSet
{
    private byte[] header;
    private byte[] body;
    
    public MessageQueueDataSet(final long timeout, final String sourceMessageQName) {
        super(timeout, sourceMessageQName);
        this.header = null;
        this.body = null;
    }
    
    @Override
    public void setBodyElement(final String key, final byte[] data) {
    }
    
    @Override
    public void setBodyElement(final byte[] data) {
        this.body = data;
    }
    
    @Override
    public byte[] getBodyElement(final String key) {
        return null;
    }
    
    @Override
    public byte[] getBodyElement() {
        return this.body;
    }
    
    @Override
    public void setHeaderElement(final String key, final byte[] header) {
    }
    
    @Override
    public void setHeaderElement(final byte[] header) {
        this.header = header;
    }
    
    @Override
    public byte[] getHeaderElement(final String key) {
        return null;
    }
    
    @Override
    public byte[] getHeaderElement() {
        return this.header;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.header = null;
        this.body = null;
    }
}
