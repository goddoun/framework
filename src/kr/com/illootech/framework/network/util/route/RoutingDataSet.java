

package kr.com.illootech.framework.network.util.route;

import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;

public class RoutingDataSet
{
    private String id;
    private String ip;
    private int port;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private Object obj;
    
    public RoutingDataSet() {
        this.id = null;
        this.ip = null;
        this.port = -1;
        this.in = null;
        this.out = null;
        this.obj = null;
    }
    
    public boolean put(final String id, final String ip, final int port, final BufferedInputStream in, final BufferedOutputStream out, final Object obj) {
        try {
            this.ip = ip;
            this.port = port;
            return this.put(id, in, out, obj);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final String ip, final int port, final BufferedInputStream in, final BufferedOutputStream out) {
        try {
            this.ip = ip;
            this.port = port;
            return this.put(id, in, out);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final BufferedInputStream in, final BufferedOutputStream out, final Object obj) {
        try {
            this.id = id;
            this.in = in;
            this.out = out;
            this.obj = obj;
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final BufferedInputStream in, final BufferedOutputStream out) {
        try {
            this.id = id;
            this.in = in;
            this.out = out;
            this.obj = null;
            return true;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id) {
        try {
            return this.put(id, null, null);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final BufferedInputStream in) {
        try {
            return this.put(id, in, null);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean put(final String id, final BufferedOutputStream out) {
        try {
            return this.put(id, null, out);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public String getID() {
        return this.id;
    }
    
    public String getIP() {
        return this.ip;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public BufferedInputStream getInputStream() {
        return this.in;
    }
    
    public BufferedOutputStream getOutputStream() {
        return this.out;
    }
    
    public Object getObject() {
        return this.obj;
    }
}
