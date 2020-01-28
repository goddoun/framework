

package kr.com.illootech.framework.collection.messageq.linear.basic.dataset;

import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;

public class MessageQueueDataSet
{
    private byte[] payload;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;
    
    public MessageQueueDataSet() {
        this.payload = null;
        this.bis = null;
        this.bos = null;
    }
    
    public boolean setPayload(final byte[] data) {
        boolean result = false;
        try {
            if (data == null) {
                return result;
            }
            this.payload = data;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public byte[] getPayload() {
        return this.payload;
    }
    
    public void setBIStream(final BufferedInputStream bis) {
        this.bis = bis;
    }
    
    public BufferedInputStream getBIStream() {
        return this.bis;
    }
    
    public void setBOStream(final BufferedOutputStream bos) {
        this.bos = bos;
    }
    
    public BufferedOutputStream getBOStream() {
        return this.bos;
    }
}
