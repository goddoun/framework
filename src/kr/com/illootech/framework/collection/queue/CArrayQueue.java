

package kr.com.illootech.framework.collection.queue;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;

public class CArrayQueue implements IFQueue
{
    protected int FRONT;
    protected int REAR;
    protected int LIMIT;
    private String procname;
    private Object[] queue;
    
    public CArrayQueue(final int maxSize) {
        this.FRONT = 0;
        this.REAR = -1;
        this.LIMIT = 0;
        this.procname = null;
        this.queue = null;
        this.LIMIT = maxSize + 1;
    }
    
    @Override
    public boolean init(final String queueName) {
        boolean result = false;
        try {
            if (queueName == null || queueName.equals("")) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. circular-queue with array[Object]. Argument is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            this.procname = queueName;
            this.queue = new Object[this.LIMIT];
            if (this.queue != null) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. circular-queue with array[Object]] - %3$s", this.procname, result ? "RUNNING" : "ERROR", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public void release() {
        try {
            this.queue = null;
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released circular-queue with array[Object]]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public boolean push(final Object data) {
        boolean result = false;
        try {
            if (this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the data. Queue is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            if (!this.isFull()) {
                if (this.REAR == this.LIMIT - 1) {
                    this.REAR = -1;
                }
                this.queue[++this.REAR] = data;
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the object[ '%3$s' ] data.] - %4$s", this.procname, result ? "RUNNING" : "ERROR", (data != null) ? data.getClass().getSimpleName() : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public Object poll() {
        Object result = null;
        try {
            if (this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Poll the object data. Queue is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            if (!this.isEmpty()) {
                result = this.queue[this.FRONT++];
                if (this.FRONT == this.LIMIT) {
                    this.FRONT = 0;
                }
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Poll the object[ '%3$s' ] data.] - %4$s", this.procname, (result != null) ? "RUNNING" : "ERROR", (result != null) ? result.getClass().getSimpleName() : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    private boolean isEmpty() {
        boolean result = false;
        try {
            if (this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Check the queue status. Queue is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            if (this.REAR == this.FRONT - 1) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            if (result) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Queue is empty. Front.pos '%3$d' -- Rear.pos '%4$d']", this.procname, "RUNNING", this.FRONT, this.REAR), LoggerElements.LOG_LEVEL2);
            }
        }
    }
    
    private boolean isFull() {
        boolean result = false;
        try {
            if (this.queue == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Check the queue status. Queue is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            if (this.REAR == this.FRONT - 2 || this.REAR == this.FRONT + this.LIMIT - 2) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            if (result && result) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Queue is full. Front.pos '%3$d' -- Rear.pos '%4$d']", this.procname, "RUNNING", this.FRONT, this.REAR), LoggerElements.LOG_LEVEL2);
            }
        }
    }
    
    @Override
    public long getLimit() {
        return this.LIMIT;
    }
    
    @Override
    public long getFreeSpace() {
        return this.LIMIT - (this.REAR - this.FRONT) + 1;
    }
    
    @Override
    public void clear() {
        this.FRONT = 0;
        this.REAR = -1;
    }
    
    @Override
    public String getQueueName() {
        return this.procname;
    }
    
    @Override
    public long getFrontPos() {
        return this.FRONT;
    }
    
    @Override
    public long getRearPos() {
        return this.REAR;
    }
    
    public static void main(final String[] args) {
        try {
            LoggerElements.setLevel(2);
            final CArrayQueue q = new CArrayQueue(5);
            q.init("CQ-TEST");
            q.push("1");
            q.push("2");
            q.push("acidhan #1");
            q.push("acidhan #2");
            final byte[] tmpByte = { 97, 98 };
            q.push(tmpByte);
            q.push("acidhan #4");
            q.printQ();
            for (int i = 0; i < 5; ++i) {
                final Object result = q.poll();
                if (result instanceof String) {
                    System.out.println("Result is '" + result + "'");
                }
                else if (result instanceof byte[]) {
                    System.out.println("Result Byte[] is '" + new String((byte[])result) + "'");
                }
                q.printQ();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void printQ() {
        try {
            final int size = this.queue.length;
            System.out.println("--------------------------------------------------------------------------");
            for (int i = 0; i < size; ++i) {
                System.out.println("*INDEX[" + i + "] DATA[" + this.queue[i] + "]");
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
}
