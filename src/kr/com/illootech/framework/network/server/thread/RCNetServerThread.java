

package kr.com.illootech.framework.network.server.thread;

import java.io.IOException;
import java.net.BindException;
import kr.com.illootech.framework.file.log.Logger;
import java.net.ServerSocket;
import kr.com.illootech.framework.thread.RCThread;

public class RCNetServerThread extends RCThread
{
    protected ServerSocket serverSock;
    
    public RCNetServerThread() {
        this.serverSock = null;
    }
    
    public boolean initServer(final int openedPort) {
        try {
            return this.createServerSocket(openedPort);
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public void releaseServerSocket() {
        this.destroyServerSocket();
    }
    
    private boolean createServerSocket(final int port) {
        try {
            this.serverSock = new ServerSocket(port);
            if (this.serverSock == null) {
                return false;
            }
            this.serverSock.setReuseAddress(true);
            return true;
        }
        catch (BindException be) {
            Logger.error(be);
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
    
    private boolean destroyServerSocket() {
        try {
            if (this.serverSock != null) {
                this.serverSock.close();
            }
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
        finally {
            this.serverSock = null;
        }
    }
}
