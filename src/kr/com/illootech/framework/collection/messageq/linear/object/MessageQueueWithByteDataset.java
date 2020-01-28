

package kr.com.illootech.framework.collection.messageq.linear.object;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.collection.messageq.linear.object.dataset.bytes.MessageQueueDataSet;
import java.util.LinkedList;

public class MessageQueueWithByteDataset
{
    final String procname = "MQ.RSLTDS";
    private String qname;
    private long currPtr;
    private long limit;
    private LinkedList<MessageQueueDataSet> QUEUE;
    
    public MessageQueueWithByteDataset() {
        this.qname = null;
        this.currPtr = 0L;
        this.limit = 0L;
        this.QUEUE = null;
    }
    
    public boolean init(final String mqName, final long limit) {
        boolean result = false;
        try {
            if (limit <= 0L) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. virtual mapping memory on disk. Limitation range is between 0 to 1000000] - FAIL", "MQ.RSLTDS", "ERROR"), LoggerElements.LOG_LEVEL4);
                return result;
            }
            this.qname = mqName;
            this.limit = limit;
            this.QUEUE = new LinkedList<MessageQueueDataSet>();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            if (!result) {
                this.QUEUE = null;
            }
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. message.queue '%3$s' with 'ResultPacketDataset'. Limit '%4$d'] - %5$s", "MQ.RSLTDS", result ? "RUNNING" : "ERROR", this.qname, this.limit, result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public synchronized boolean put(final MessageQueueDataSet resultPacketDataset) {
        boolean result = false;
        try {
            synchronized (this.QUEUE) {
                if (this.currPtr <= this.limit) {
                    if (this.QUEUE.add(resultPacketDataset)) {
                        ++this.currPtr;
                        result = true;
                    }
                }
                else {
                    Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Message queue '%3$s' is full] - FAIL", "MQ.RSLTDS", "ERROR", this.qname), LoggerElements.LOG_LEVEL4);
                }
                // monitorexit(this.QUEUE)
                return result;
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Put the data to message.Q. MQ.name '%3$s'] - %4$s", "MQ.RSLTDS", result ? "RUNNING" : "ERROR", (this.qname != null) ? this.qname : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public synchronized MessageQueueDataSet poll() {
        MessageQueueDataSet result = null;
        try {
            synchronized (this.QUEUE) {
                result = this.QUEUE.pollFirst();
                if (result != null) {
                    --this.currPtr;
                }
            }
            // monitorexit(this.QUEUE)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Poll the dataset from message.Q. MQ.name '%3$s'] - %4$s", "MQ.RSLTDS", (result != null) ? "RUNNING" : "ERROR", this.qname, (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public String getQueueName() {
        return this.qname;
    }
    
    public synchronized void clear() {
        try {
            synchronized (this.QUEUE) {
                this.QUEUE.clear();
            }
            // monitorexit(this.QUEUE)
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Clear message.Q] - SUCCESS", "MQ.RSLTDS", "RUNNING"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public long getQueueUsage() {
        return this.currPtr;
    }
    
    public long getLimit() {
        return this.limit;
    }
}
