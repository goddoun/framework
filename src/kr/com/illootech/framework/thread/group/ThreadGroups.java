

package kr.com.illootech.framework.thread.group;

import java.util.Iterator;
import java.util.Set;
import java.util.LinkedList;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.thread.IFRCThread;
import java.util.HashMap;

public class ThreadGroups
{
    private final String procname = "THD.GRPs";
    private int ACTIVATED_THD_COUNT_IN_GROUP;
    private int groupId;
    private String groupName;
    private HashMap<String, IFRCThread> groupMap;
    
    public ThreadGroups() {
        this.ACTIVATED_THD_COUNT_IN_GROUP = 0;
        this.groupId = -1;
        this.groupName = null;
        this.groupMap = null;
    }
    
    public boolean init(final int id, final String name) {
        boolean result = false;
        try {
            this.groupId = id;
            this.groupName = name;
            this.groupMap = new HashMap<String, IFRCThread>();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized void setThreadCountToActivate(final int count) {
        try {
            this.ACTIVATED_THD_COUNT_IN_GROUP = count;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public int getThreadCountToActivateInThdGroup() {
        return this.ACTIVATED_THD_COUNT_IN_GROUP;
    }
    
    public synchronized boolean join(final String threadName, final IFRCThread thread) {
        boolean result = false;
        try {
            if (threadName == null || thread == null) {
                return result;
            }
            if (this.groupMap.containsKey(threadName)) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][ '%3$s' thread is already contained group.map]", "THD.GRPs", "SKIP", (threadName != null) ? threadName : "N/A"), LoggerElements.LOG_LEVEL1);
                return result;
            }
            if (this.ACTIVATED_THD_COUNT_IN_GROUP < this.groupMap.size()) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][ '%3$s' thread limit is overloaded. GroupMap '%4$d' < Map '%5$d']", "THD.GRPs", "SKIP", (threadName != null) ? threadName : "N/A", this.ACTIVATED_THD_COUNT_IN_GROUP, this.groupMap.size()), LoggerElements.LOG_LEVEL1);
            }
            synchronized (this.groupMap) {
                if (this.groupMap.containsKey(threadName)) {
                    result = true;
                }
                else {
                    this.groupMap.put(threadName, thread);
                    if (this.groupMap.containsKey(threadName)) {
                        result = true;
                    }
                }
            }
            // monitorexit(this.groupMap)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized boolean exist(final String threadName) {
        boolean result = false;
        try {
            synchronized (this.groupMap) {
                if (this.groupMap.containsKey(threadName)) {
                    result = true;
                }
            }
            // monitorexit(this.groupMap)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public synchronized boolean leave(final String threadName) {
        boolean result = false;
        try {
            if (threadName == null) {
                return result;
            }
            synchronized (this.groupMap) {
                this.groupMap.remove(threadName);
                if (!this.groupMap.containsKey(threadName)) {
                    result = true;
                }
            }
            // monitorexit(this.groupMap)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean keepAlive(final int threadId) {
        boolean result = false;
        try {
            if (threadId < 0) {
                return result;
            }
            if (threadId < this.ACTIVATED_THD_COUNT_IN_GROUP) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public int getGroupId() {
        return this.groupId;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public IFRCThread getThread(final String threadname) {
        IFRCThread result = null;
        try {
            if (threadname != null) {
                result = this.groupMap.get(threadname);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public LinkedList<String> getThreadNameList() {
        LinkedList<String> result = null;
        try {
            if (this.groupMap == null) {
                return result;
            }
            final Set<String> keys = this.groupMap.keySet();
            if (keys == null) {
                return result;
            }
            final Iterator<String> it = keys.iterator();
            if (it == null) {
                return result;
            }
            result = new LinkedList<String>();
            while (it.hasNext()) {
                final String key = it.next();
                if (key == null) {
                    continue;
                }
                result.addLast(key);
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public HashMap<String, IFRCThread> getGroup() {
        return this.groupMap;
    }
    
    public int getTotalRegisteredThreadSizeInGroup() {
        return this.groupMap.size();
    }
}
