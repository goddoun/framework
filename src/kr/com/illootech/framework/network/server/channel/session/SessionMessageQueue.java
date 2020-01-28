

package kr.com.illootech.framework.network.server.channel.session;

import kr.com.illootech.framework.file.log.Logger;
import java.util.LinkedList;

public class SessionMessageQueue
{
    private long DEFAULT_QUEUE_LIMIT_SIZE;
    private LinkedList<SessionDataset> queue;
    
    public SessionMessageQueue() {
        this.DEFAULT_QUEUE_LIMIT_SIZE = -1L;
        this.queue = null;
    }
    
    public boolean init(final long limit) {
        boolean result = false;
        try {
            this.DEFAULT_QUEUE_LIMIT_SIZE = limit;
            if (this.queue == null) {
                this.queue = new LinkedList<SessionDataset>();
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public SessionDataset get() {
        final SessionDataset result = null;
        try {
            if (this.queue == null || this.queue.size() == 0) {
                return result;
            }
            synchronized (this.queue) {
                // monitorexit(this.queue)
                return this.queue.poll();
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public boolean put(final SessionDataset dataset) {
        boolean result = false;
        try {
            if (this.queue == null || this.queue.size() > this.DEFAULT_QUEUE_LIMIT_SIZE) {
                return result;
            }
            synchronized (this.queue) {
                this.queue.push(dataset);
                result = true;
            }
            // monitorexit(this.queue)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public long size() {
        return this.queue.size();
    }
}
