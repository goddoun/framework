

package kr.com.illootech.framework.utils.reflect.map;

import java.util.HashMap;

import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import kr.com.illootech.framework.utils.reflect.DynamicClassLoader;

public class DynamicClassLoaderMap
{
    private final String procname = "DCLASS.MAP";
    private HashMap<String, DynamicClassLoader> LoadersMap;
    
    public DynamicClassLoaderMap() {
        this.LoadersMap = null;
    }
    
    public boolean init() {
        boolean result = false;
        try {
            this.LoadersMap = new HashMap<String, DynamicClassLoader>();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public void release() {
        try {
            if (this.LoadersMap != null) {
                this.LoadersMap.clear();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.LoadersMap = null;
        }
    }
    
    public synchronized boolean set(final String key, final DynamicClassLoader loader) {
        boolean result = false;
        try {
            if (key == null || loader == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Set. Arguments is not available. key '%3$s', dataset '%4$s'] - FAIL", "DCLASS.MAP", "ERROR", (key != null) ? key : "N/A", (loader != null) ? loader.getClass().getName() : "N/A"), LoggerElements.LOG_LEVEL4);
                return result;
            }
            synchronized (this.LoadersMap) {
                this.LoadersMap.put(key, loader);
                if (this.LoadersMap.containsKey(key)) {
                    result = true;
                }
            }
            // monitorexit(this.LoadersMap)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Set dataset. key '%3$s'] - %4$s", "DCLASS.MAP", result ? "RUNNING" : "ERROR", (key != null) ? key : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public synchronized DynamicClassLoader get(final String key) {
        DynamicClassLoader result = null;
        try {
            if (key == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get. Arguments is not available. key '%3$s'] - FAIL", "DCLASS.MAP", "ERROR", (key != null) ? key : "N/A"), LoggerElements.LOG_LEVEL4);
                return result;
            }
            synchronized (this.LoadersMap) {
                result = this.LoadersMap.get(key);
            }
            // monitorexit(this.LoadersMap)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get dataset. key '%3$s'] - %4$s", "DCLASS.MAP", (result != null) ? "RUNNING" : "ERROR", (key != null) ? key : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public boolean exist(final String key) {
        boolean result = false;
        try {
            if (key == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Exist. Arguments is not available. key '%3$s'] - FAIL", "DCLASS.MAP", "ERROR", (key != null) ? key : "N/A"), LoggerElements.LOG_LEVEL4);
                return result;
            }
            synchronized (this.LoadersMap) {
                result = this.LoadersMap.containsKey(key);
            }
            // monitorexit(this.LoadersMap)
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Exist dataset. key '%3$s'] - %4$s", "DCLASS.MAP", result ? "RUNNING" : "ERROR", (key != null) ? key : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL4);
        }
    }
    
    public synchronized void remove(final String key) {
        try {
            if (key == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Remove. Arguments is not available. key '%3$s'] - FAIL", "DCLASS.MAP", "ERROR", (key != null) ? key : "N/A"), LoggerElements.LOG_LEVEL4);
                return;
            }
            synchronized (this.LoadersMap) {
                if (this.LoadersMap.containsKey(key)) {
                    this.LoadersMap.remove(key);
                }
            }
            // monitorexit(this.LoadersMap)
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Remove dataset. key '%3$s'] - SUCCESS", "DCLASS.MAP", "RUNNING", (key != null) ? key : "N/A"), LoggerElements.LOG_LEVEL4);
        }
    }
}
