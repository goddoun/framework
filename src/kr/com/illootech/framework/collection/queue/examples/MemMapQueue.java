

package kr.com.illootech.framework.collection.queue.examples;

import java.io.File;
import java.io.IOException;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.RandomAccessFile;
import kr.com.illootech.framework.collection.queue.IFQueue;

public class MemMapQueue implements IFQueue
{
    private String procname;
    private final String FILE_OPEN_RW = "rw";
    private int HEAD;
    private int TAIL;
    private long LIMIT;
    private int PREFIX_LENGTH_FLD_SIZE;
    private RandomAccessFile memRAFile;
    private FileChannel fChannel;
    private ByteBuffer mappedBuffer;
    protected String queueFilename;
    
    public MemMapQueue(final long limit) {
        this.procname = "MMAP.MQ";
        this.HEAD = 0;
        this.TAIL = 0;
        this.LIMIT = 0L;
        this.PREFIX_LENGTH_FLD_SIZE = 4;
        this.memRAFile = null;
        this.fChannel = null;
        this.mappedBuffer = null;
        this.queueFilename = "cmq.mem";
        this.LIMIT = limit;
        this.PREFIX_LENGTH_FLD_SIZE = 4;
    }
    
    public MemMapQueue(final String mmapFile, final long limit) {
        this.procname = "MMAP.MQ";
        this.HEAD = 0;
        this.TAIL = 0;
        this.LIMIT = 0L;
        this.PREFIX_LENGTH_FLD_SIZE = 4;
        this.memRAFile = null;
        this.fChannel = null;
        this.mappedBuffer = null;
        this.queueFilename = "cmq.mem";
        this.queueFilename = mmapFile;
        this.LIMIT = limit;
        this.PREFIX_LENGTH_FLD_SIZE = 4;
    }
    
    @Override
    public boolean init(final String queueName) {
        boolean result = false;
        try {
            if (this.queueFilename == null || this.queueFilename.equals("")) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. mem-map-queue. Arguments is not available] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            this.procname = queueName;
            this.memRAFile = new RandomAccessFile(this.queueFilename, "rw");
            this.fChannel = this.memRAFile.getChannel();
            this.mappedBuffer = this.fChannel.map(FileChannel.MapMode.READ_WRITE, 0L, this.LIMIT);
            final int n = 0;
            this.TAIL = n;
            this.HEAD = n;
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Init. mem-map-queue. '%3$s'] - %4$s", this.procname, result ? "RUNNING" : "ERROR", (this.queueFilename != null) ? this.queueFilename : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
            if (!result) {
                try {
                    this.memRAFile.close();
                }
                catch (IOException ie) {
                    Logger.error(ie);
                }
            }
        }
    }
    
    @Override
    public boolean push(final Object data) {
        boolean result = false;
        try {
            if (data instanceof byte[]) {
                result = this.push(data);
            }
            else if (data instanceof ByteBuffer) {
                result = this.push(data);
            }
            else {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the data. Not supported type : Only byte[] or ByteBuffer]", this.procname, result ? "RUNNING" : "ERROR"), LoggerElements.LOG_LEVEL2);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    private synchronized boolean push(final byte[] buf) {
        boolean result = false;
        ByteBuffer tmpBuf = null;
        try {
            tmpBuf = ByteBuffer.wrap(buf);
            result = this.push(tmpBuf);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            tmpBuf = null;
        }
    }
    
    private synchronized boolean push(final ByteBuffer buf) {
        boolean result = false;
        long freeSpace = 0L;
        try {
            if (buf == null || buf.limit() <= 0 || this.isFull()) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the data. Data is not available or Full] - FAIL", this.procname, "ERROR"), LoggerElements.LOG_LEVEL2);
                return result;
            }
            synchronized (this.fChannel) {
                freeSpace = this.getFreeSpace();
                if (freeSpace > buf.limit()) {
                    this.mappedBuffer.position(this.TAIL);
                    final int len = buf.limit();
                    this.mappedBuffer.putInt(len);
                    this.mappedBuffer.put(buf);
                    this.TAIL += buf.limit() + this.PREFIX_LENGTH_FLD_SIZE;
                    result = true;
                }
            }
            // monitorexit(this.fChannel)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Push the data. 'HD %3$d --- TL %4$d' '%5$s' '%6$d' byte(s)] - %7$s", this.procname, result ? "RUNNING" : "ERROR", this.HEAD, this.TAIL, (buf != null) ? new String(buf.array()) : "N/A", (buf != null) ? Integer.valueOf(buf.limit()) : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public synchronized byte[] poll() {
        byte[] result = null;
        try {
            if (this.isEmpty()) {
                return result;
            }
            synchronized (this.fChannel) {
                final int dataSize = this.mappedBuffer.getInt(this.HEAD);
                if (dataSize < 0) {
                    // monitorexit(this.fChannel)
                    return result;
                }
                result = new byte[dataSize];
                this.HEAD += this.PREFIX_LENGTH_FLD_SIZE;
                this.mappedBuffer.position(this.HEAD);
                this.mappedBuffer.get(result);
                this.HEAD += dataSize;
                // monitorexit(this.fChannel)
                return result;
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Poll the data. 'HD %3$d --- TL %4$d' '%5$s' '%6$d' byte(s)] - %7$s", this.procname, (result != null) ? "RUNNING" : "ERROR", this.HEAD, this.TAIL, (result != null) ? new String(result) : "N/A", (result != null) ? result.length : -1, (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL2);
        }
    }
    
    @Override
    public synchronized void clear() {
        try {
            synchronized (this.fChannel) {
                this.mappedBuffer.position(0);
                this.HEAD = 0;
                this.TAIL = 0;
            }
            // monitorexit(this.fChannel)
        }
        catch (Exception e) {
            Logger.error(e);
            return;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Cleared queue]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
        }
        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Cleared queue]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
    }
    
    public synchronized void delete() {
        try {
            synchronized (this.fChannel) {
                this.release();
                final File file = new File(this.queueFilename);
                if (file.exists() && file.canRead()) {
                    file.delete();
                }
            }
            // monitorexit(this.fChannel)
        }
        catch (Exception e) {
            Logger.error(e);
            return;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Deleted mem-map-file '%3$s']", this.procname, "RUNNING", (this.queueFilename != null) ? this.queueFilename : "N/A"), LoggerElements.LOG_LEVEL2);
        }
        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Deleted mem-map-file '%3$s']", this.procname, "RUNNING", (this.queueFilename != null) ? this.queueFilename : "N/A"), LoggerElements.LOG_LEVEL2);
    }
    
    @Override
    public synchronized void release() {
        try {
            synchronized (this.fChannel) {
                if (this.mappedBuffer != null) {
                    this.mappedBuffer.clear();
                }
                if (this.fChannel != null) {
                    this.fChannel.close();
                }
                this.fChannel = null;
                if (this.memRAFile != null) {
                    this.memRAFile.close();
                }
                this.memRAFile = null;
            }
            // monitorexit(this.fChannel)
        }
        catch (Exception e) {
            Logger.error(e);
            return;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released queue-resource]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
        }
        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Released queue-resource]", this.procname, "RUNNING"), LoggerElements.LOG_LEVEL2);
    }
    
    private boolean isEmpty() {
        return this.HEAD == this.TAIL;
    }
    
    private boolean isFull() {
        return this.LIMIT == this.TAIL - this.HEAD;
    }
    
    @Override
    public long getFreeSpace() {
        return this.LIMIT - (this.TAIL - this.HEAD);
    }
    
    @Override
    public long getFrontPos() {
        return this.HEAD;
    }
    
    @Override
    public long getRearPos() {
        return this.TAIL;
    }
    
    @Override
    public long getLimit() {
        return this.LIMIT;
    }
    
    @Override
    public String getQueueName() {
        return this.queueFilename;
    }
    
    public static void main(final String[] args) {
        try {
            LoggerElements.setLevel(2);
            final MemMapQueue mmq = new MemMapQueue("test", 60L);
            if (!mmq.init("MM.MQ")) {
                System.out.println("init-fail");
            }
            for (int i = 0; i < 10; ++i) {
                mmq.push(String.valueOf(i).getBytes());
            }
            mmq.push("aci".getBytes());
            mmq.push("test".getBytes());
            for (int i = 0; i < 20; ++i) {
                final byte[] tmpByte = mmq.poll();
                if (tmpByte == null) {}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
