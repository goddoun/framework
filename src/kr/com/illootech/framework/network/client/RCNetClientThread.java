

package kr.com.illootech.framework.network.client;

import java.io.IOException;
import java.net.UnknownHostException;
import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.net.Socket;
import kr.com.illootech.framework.thread.IFRCThread;
import kr.com.illootech.framework.thread.RCThread;

public class RCNetClientThread extends RCThread implements IFRCThread
{
    protected Socket clientSock;
    protected BufferedInputStream in;
    protected BufferedOutputStream out;
    
    public RCNetClientThread() {
        this.clientSock = null;
        this.in = null;
        this.out = null;
    }
    
    public boolean connectToHost(final String ip, final int port) {
        try {
            if (ip != null && port != 0) {
                (this.clientSock = new Socket(ip, port)).setReuseAddress(true);
                return true;
            }
            return false;
        }
        catch (UnknownHostException uhe) {
            Logger.error(uhe);
            return false;
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
    
    public boolean initStream() {
        try {
            this.in = new BufferedInputStream(this.clientSock.getInputStream());
            this.out = new BufferedOutputStream(this.clientSock.getOutputStream());
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
            if (this.clientSock != null) {
                this.clientSock.close();
            }
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.clientSock = null;
        }
        this.clientSock = null;
    }
}
