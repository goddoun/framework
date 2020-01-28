

package kr.com.illootech.framework.collection.messageq.linear;

import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedHashMap;

public class RCMessageDataSet implements IF_MessageDataSet
{
    private String messageId;
    private byte[] sourcePacket;
    private LinkedHashMap<String, byte[]> header;
    private LinkedHashMap<String, byte[]> payload;
    
    public RCMessageDataSet() {
        this.messageId = null;
        this.sourcePacket = null;
        this.header = null;
        this.payload = null;
    }
    
    @Override
    public boolean init() {
        boolean result = false;
        try {
            this.header = new LinkedHashMap<String, byte[]>();
            this.payload = new LinkedHashMap<String, byte[]>();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public void release() {
        try {
            if (this.header != null) {
                this.header.clear();
            }
            if (this.payload != null) {
                this.payload.clear();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.header = null;
            this.payload = null;
        }
    }
    
    @Override
    public boolean setMessageId(final String id) {
        boolean result = false;
        try {
            this.messageId = id;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public String getMessageId() {
        return this.messageId;
    }
    
    @Override
    public boolean setSourcePacket(final byte[] packet) {
        boolean result = false;
        try {
            this.sourcePacket = packet;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public byte[] getSourcePacket() {
        return this.sourcePacket;
    }
    
    @Override
    public boolean setHeader(final LinkedHashMap<String, byte[]> data) {
        boolean result = false;
        try {
            if (data == null || data.size() <= 0) {
                return result;
            }
            this.header = data;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public LinkedHashMap<String, byte[]> getHeader() {
        LinkedHashMap<String, byte[]> result = null;
        try {
            result = this.header;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    @Override
    public boolean setPayload(final LinkedHashMap<String, byte[]> data) {
        boolean result = false;
        try {
            if (data == null || data.size() <= 0) {
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
    
    @Override
    public LinkedHashMap<String, byte[]> getPayload() {
        LinkedHashMap<String, byte[]> result = null;
        try {
            result = this.payload;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
