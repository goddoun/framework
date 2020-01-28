

package kr.com.illootech.framework.collection.messageq.linear;

import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;

public class RCMessageQueue implements IF_MessageQueue
{
    private String qname;
    private LinkedList<IF_MessageDataSet> messageQueue;
    
    public RCMessageQueue() {
        this.qname = null;
        this.messageQueue = null;
    }
    
    @Override
    public boolean init(final String qname) {
        boolean result = false;
        try {
            this.qname = qname;
            this.messageQueue = new LinkedList<IF_MessageDataSet>();
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
            if (this.messageQueue != null) {
                this.messageQueue.clear();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.messageQueue = null;
            this.qname = null;
        }
    }
    
    @Override
    public boolean put(final IF_MessageDataSet dataset) {
        boolean result = false;
        try {
            synchronized (this.messageQueue) {
                this.messageQueue.add(dataset);
                result = this.messageQueue.contains(dataset);
            }
            // monitorexit(this.messageQueue)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    @Override
    public IF_MessageDataSet poll() {
        IF_MessageDataSet result = null;
        try {
            synchronized (this.messageQueue) {
                result = this.messageQueue.pollFirst();
            }
            // monitorexit(this.messageQueue)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    @Override
    public int qSize() {
        int result = -1;
        try {
            result = this.messageQueue.size();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    @Override
    public String getQName() {
        return this.qname;
    }
}
