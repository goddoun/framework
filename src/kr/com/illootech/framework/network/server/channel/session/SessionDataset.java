

package kr.com.illootech.framework.network.server.channel.session;

import java.nio.channels.SelectionKey;

public class SessionDataset
{
    private SelectionKey key;
    private byte[] data;
    
    public SessionDataset() {
        this.key = null;
        this.data = null;
    }
    
    public boolean set(final SelectionKey key, final byte[] data) {
        boolean result = false;
        try {
            this.key = key;
            this.data = data;
            result = true;
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean set(final SelectionKey key) {
        boolean result = false;
        try {
            this.key = key;
            result = true;
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public SelectionKey getKey() {
        return this.key;
    }
    
    public byte[] getData() {
        return this.data;
    }
}
