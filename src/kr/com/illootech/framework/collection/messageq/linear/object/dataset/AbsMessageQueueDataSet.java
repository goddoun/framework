

package kr.com.illootech.framework.collection.messageq.linear.object.dataset;

import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.util.Date;

public abstract class AbsMessageQueueDataSet
{
    private String primaryKey;
    private Date srcTime;
    private long timeout;
    private boolean regToTimerRepositorySucc;
    private String sourceMessageQueueName;
    private String destinationMessageQueueName;
    private BufferedInputStream sourceInSession;
    private BufferedOutputStream sourceOutSession;
    private BufferedInputStream destInSession;
    private BufferedOutputStream destOutSession;
    private String sendingCode;
    
    public AbsMessageQueueDataSet(final long timeout, final String sourceMessageQName) {
        this.primaryKey = null;
        this.srcTime = null;
        this.timeout = 0L;
        this.regToTimerRepositorySucc = false;
        this.sourceMessageQueueName = null;
        this.destinationMessageQueueName = null;
        this.sourceInSession = null;
        this.sourceOutSession = null;
        this.destInSession = null;
        this.destOutSession = null;
        this.sendingCode = null;
        this.timeout = timeout;
        this.srcTime = new Date(System.currentTimeMillis());
        this.sourceMessageQueueName = sourceMessageQName;
    }
    
    public long getTimeout() {
        return this.timeout;
    }
    
    public void setPrimaryKey(final String key) {
        this.primaryKey = key;
    }
    
    public String getPrimaryKey() {
        return this.primaryKey;
    }
    
    public void setRegDataToTimerRepositorySucc(final boolean succ) {
        this.regToTimerRepositorySucc = succ;
    }
    
    public boolean getRegDataToTimerRepositorySucc() {
        return this.regToTimerRepositorySucc;
    }
    
    public void setSourceMessageQName(final String qname) {
        this.sourceMessageQueueName = qname;
    }
    
    public String getSourceMessageQName() {
        return this.sourceMessageQueueName;
    }
    
    public void setDestMessageQName(final String qname) {
        this.destinationMessageQueueName = qname;
    }
    
    public String getDestMessageQName() {
        return this.destinationMessageQueueName;
    }
    
    public void setSourceSession(final BufferedInputStream in, final BufferedOutputStream out) {
        this.sourceInSession = in;
        this.sourceOutSession = out;
    }
    
    public BufferedInputStream getSourceInputStream() {
        return this.sourceInSession;
    }
    
    public BufferedOutputStream getSourceOutputStream() {
        return this.sourceOutSession;
    }
    
    public void setDestSession(final BufferedInputStream in, final BufferedOutputStream out) {
        this.destInSession = in;
        this.destOutSession = out;
    }
    
    public BufferedInputStream getDestInputStream() {
        return this.destInSession;
    }
    
    public BufferedOutputStream getDestOutputStream() {
        return this.destOutSession;
    }
    
    public boolean sendToSourceSession(final byte[] data) {
        final boolean result = false;
        try {
            if (data == null || this.sourceInSession == null || this.sourceOutSession == null) {
                return result;
            }
            synchronized (this.sourceOutSession) {
                this.sourceOutSession.write(data);
                this.sourceOutSession.flush();
            }
            // monitorexit(this.sourceOutSession)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean sendToDestSession(final byte[] data) {
        final boolean result = false;
        try {
            if (data == null || this.destInSession == null || this.destOutSession == null) {
                return result;
            }
            synchronized (this.destOutSession) {
                this.destOutSession.write(data);
                this.destOutSession.flush();
            }
            // monitorexit(this.destOutSession)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public void setSendingCode(final String code) {
        this.sendingCode = code;
    }
    
    public String getSendingCode() {
        return this.sendingCode;
    }
    
    public abstract void setBodyElement(final String p0, final byte[] p1);
    
    public abstract void setBodyElement(final byte[] p0);
    
    public abstract byte[] getBodyElement(final String p0);
    
    public abstract byte[] getBodyElement();
    
    public abstract void setHeaderElement(final String p0, final byte[] p1);
    
    public abstract void setHeaderElement(final byte[] p0);
    
    public abstract byte[] getHeaderElement(final String p0);
    
    public abstract byte[] getHeaderElement();
    
    public void setCurrentTime() {
        Date time = null;
        try {
            time = new Date(System.currentTimeMillis());
            this.srcTime = time;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public Date getCurrentTime() {
        return this.srcTime;
    }
    
    public void clear() {
        try {
            this.primaryKey = null;
            this.srcTime = null;
            this.sendingCode = null;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public boolean isTimeout() {
        boolean result = false;
        long diff = 0L;
        try {
            if (this.timeout <= 0L) {
                return result;
            }
            final Date targetTime = new Date(System.currentTimeMillis());
            diff = targetTime.getTime() - this.srcTime.getTime();
            if (diff >= this.timeout) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
}
