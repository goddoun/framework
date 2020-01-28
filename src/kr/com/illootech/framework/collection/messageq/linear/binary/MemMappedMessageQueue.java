

package kr.com.illootech.framework.collection.messageq.linear.binary;

import java.io.File;
import java.io.IOException;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.RandomAccessFile;

public class MemMappedMessageQueue
{
    final String procname = "MMAPED.MQ";
    private final int LIMIT_RECORD_COUNT = 1000000;
    public static final int PERCENTAGE = 0;
    public static final int BYTE = 1;
    private int header;
    private int tail;
    private int limit;
    private int capacity;
    private int currentQueueSize;
    protected String memoryFilename;
    private RandomAccessFile memRAFile;
    private FileChannel fChannel;
    private ByteBuffer mappedBuffer;
    protected int RECORD_SIZE;
    protected int RECORD_LIMIT_COUNT;
    protected int BLOCKSIZE;
    
    public MemMappedMessageQueue() {
        this.header = 0;
        this.tail = 0;
        this.limit = 0;
        this.capacity = 0;
        this.currentQueueSize = 0;
        this.memoryFilename = "cmq.mem";
        this.memRAFile = null;
        this.fChannel = null;
        this.mappedBuffer = null;
        this.RECORD_SIZE = 0;
        this.RECORD_LIMIT_COUNT = 0;
        this.BLOCKSIZE = 0;
    }
    
    public boolean init(final String memoryName, final int recordSize, final int recordCount) {
        try {
            if (recordCount > 1000000) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Create virtual mapping memory with disk. Occured overflow error. Limit  %1$s] - FAIL", "MMAPED.MQ", "ERROR", 1000000), LoggerElements.LOG_LEVEL1);
                return false;
            }
            this.memoryFilename = memoryName;
            this.RECORD_SIZE = recordSize;
            this.RECORD_LIMIT_COUNT = recordCount;
            this.BLOCKSIZE = this.RECORD_SIZE * this.RECORD_LIMIT_COUNT;
            this.memRAFile = new RandomAccessFile(memoryName, "rw");
            this.fChannel = this.memRAFile.getChannel();
            this.mappedBuffer = this.fChannel.map(FileChannel.MapMode.READ_WRITE, 0L, this.BLOCKSIZE);
            this.header = 0;
            this.tail = 0;
            this.limit = this.BLOCKSIZE;
            this.capacity = this.BLOCKSIZE;
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            try {
                this.memRAFile.close();
            }
            catch (IOException ie) {
                Logger.error(ie);
            }
        }
    }
    
    public synchronized boolean put(final byte[] buf) {
        try {
            synchronized (this.fChannel) {
                if (this.currentQueueSize >= this.RECORD_LIMIT_COUNT) {
                    // monitorexit(this.fChannel)
                    return false;
                }
                if (this.tail < this.limit) {
                    this.mappedBuffer.position(this.tail);
                    this.mappedBuffer.put(buf);
                }
                else {
                    this.tail = 0;
                    this.mappedBuffer.position(this.tail);
                    this.mappedBuffer.put(buf);
                }
                this.tail += this.RECORD_SIZE;
                ++this.currentQueueSize;
                // monitorexit(this.fChannel)
                return true;
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized boolean put(final ByteBuffer buf) {
        try {
            synchronized (this.fChannel) {
                if (this.currentQueueSize >= this.RECORD_LIMIT_COUNT) {
                    // monitorexit(this.fChannel)
                    return false;
                }
                if (this.tail < this.limit) {
                    this.mappedBuffer.position(this.tail);
                    this.mappedBuffer.put(buf);
                }
                else {
                    this.tail = 0;
                    this.mappedBuffer.position(this.tail);
                    this.mappedBuffer.put(buf);
                }
                this.tail += this.RECORD_SIZE;
                ++this.currentQueueSize;
                // monitorexit(this.fChannel)
                return true;
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized byte[] get() {
        byte[] result = null;
        try {
            synchronized (this.fChannel) {
                result = new byte[this.RECORD_SIZE];
                if (this.currentQueueSize == 0) {
                    // monitorexit(this.fChannel)
                    return null;
                }
                if (this.header < this.limit) {
                    if (this.header >= this.tail) {
                        this.currentQueueSize = 0;
                        // monitorexit(this.fChannel)
                        return null;
                    }
                    this.mappedBuffer.position(this.header);
                    this.mappedBuffer.get(result);
                }
                else {
                    this.header = 0;
                    this.mappedBuffer.position(this.header);
                    this.mappedBuffer.get(result);
                }
                this.header += this.RECORD_SIZE;
                --this.currentQueueSize;
                // monitorexit(this.fChannel)
                return result;
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public void reset() {
        try {
            synchronized (this.fChannel) {
                this.header = 0;
                this.tail = 0;
                this.mappedBuffer.position(0);
                this.limit = this.capacity;
            }
            // monitorexit(this.fChannel)
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public synchronized void clear() {
        try {
            synchronized (this.fChannel) {
                this.mappedBuffer.position(0);
                this.header = 0;
                this.tail = 0;
                this.currentQueueSize = 0;
            }
            // monitorexit(this.fChannel)
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public synchronized void delete() {
        try {
            synchronized (this.fChannel) {
                this.close();
                final File file = new File(this.memoryFilename);
                if (file.exists() && file.canRead()) {
                    file.delete();
                }
            }
            // monitorexit(this.fChannel)
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public synchronized void close() {
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
        }
    }
    
    public int getQueueUsage() {
        return this.currentQueueSize;
    }
    
    public int getLimitRecordCount() {
        return this.RECORD_LIMIT_COUNT;
    }
    
    public int getHeaderPosistion() {
        return this.header;
    }
    
    public int getTailPosition() {
        return this.tail;
    }
    
    public int getLimitPosition() {
        return this.limit;
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public String getMemoryName() {
        return this.memoryFilename;
    }
}
