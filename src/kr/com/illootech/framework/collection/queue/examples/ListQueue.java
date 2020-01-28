

package kr.com.illootech.framework.collection.queue.examples;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.util.LinkedList;
import kr.com.illootech.framework.collection.queue.IFQueue;

public class ListQueue implements IFQueue
{
    private String procname;
    private LinkedList<Object> queue;
    private long LIMIT;
    
    public ListQueue(final long maxRecord) {
        this.procname = null;
        this.queue = null;
        this.LIMIT = 0L;
        this.LIMIT = maxRecord;
    }
    
    @Override
    public boolean init(final String queueName) {
        final boolean result = false;
        try {
            if (queueName == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. queue with list. Argument is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            this.procname = queueName;
            this.queue = new LinkedList<Object>();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. queue with list. Size '%3$d'] - %4$s", this.procname, result ? "RUNNING" : "ERROR", this.LIMIT, result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public synchronized void release() {
        try {
            if (this.queue != null) {
                synchronized (this.queue) {
                    this.queue.clear();
                }
                // monitorexit(this.queue)
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.LIMIT = 0L;
            this.queue = null;
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released queue]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public synchronized boolean push(final Object data) {
        boolean result = false;
        try {
            if (data == null || this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the data. Argument or Queue is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            if (!this.isFull()) {
                synchronized (this.queue) {
                    this.queue.addLast(data);
                }
                // monitorexit(this.queue)
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the data. Free '%3$d'] - %4$s", this.procname, result ? "RUNNING" : "ERROR", this.getFreeSpace(), result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public synchronized Object poll() {
        Object result = null;
        try {
            if (this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Poll the data. Queue is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            if (!this.isEmpty()) {
                synchronized (this.queue) {
                    result = this.queue.poll();
                }
                // monitorexit(this.queue)
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Poll the data. Free '%3$d'] - %4$s", this.procname, (result != null) ? "RUNNING" : "ERROR", this.getFreeSpace(), (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    private boolean isEmpty() {
        return this.queue.size() == 0;
    }
    
    private boolean isFull() {
        return this.queue.size() == this.LIMIT;
    }
    
    @Override
    public long getLimit() {
        return this.LIMIT;
    }
    
    @Override
    public long getFreeSpace() {
        return this.LIMIT - this.queue.size();
    }
    
    @Override
    public synchronized void clear() {
        try {
            if (this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Clear the data in queue. Queue is not available ] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return;
            }
            synchronized (this.queue) {
                this.queue.clear();
            }
            // monitorexit(this.queue)
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Cleared the data in queue]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public String getQueueName() {
        return this.procname;
    }
    
    @Override
    public long getFrontPos() {
        return 0L;
    }
    
    @Override
    public long getRearPos() {
        return this.queue.size();
    }
}
