

package kr.com.illootech.framework.network.server.thread;

import java.io.IOException;
import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.net.Socket;
import kr.com.illootech.framework.thread.RCThread;

public class RCNetServerWorkerThread extends RCThread
{
    protected Socket sock;
    protected BufferedInputStream in;
    protected BufferedOutputStream out;
    
    public RCNetServerWorkerThread(final Socket sock) {
        this.sock = null;
        this.in = null;
        this.out = null;
        this.sock = sock;
    }
    
    public boolean initStream() {
        try {
            this.in = new BufferedInputStream(this.sock.getInputStream());
            this.out = new BufferedOutputStream(this.sock.getOutputStream());
            return true;
        }
        catch (IOException ie) {
            Logger.error(ie);
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public void releaseStream() {
        try {
            if (this.in != null) {
                this.in.close();
            }
            if (this.out != null) {
                this.out.close();
            }
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.in = null;
            this.out = null;
        }
        this.in = null;
        this.out = null;
    }
    
    public void releaseSocket() {
        try {
            if (this.sock != null) {
                this.sock.close();
            }
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.sock = null;
        }
        this.sock = null;
    }
}
