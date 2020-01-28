

package kr.com.illootech.framework.network.server.thread;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.net.SocketAddress;
import kr.com.illootech.framework.file.log.Logger;
import java.net.InetAddress;
import java.nio.channels.DatagramChannel;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import kr.com.illootech.framework.thread.RCThread;

public class RCNetMulticastServerTH extends RCThread
{
    public DatagramSocket socket;
    public DatagramPacket packet;
    public DatagramChannel channel;
    public InetAddress address;
    public int DEFAULT_MULTICAST_PORT;
    public String DEFAULT_MULTICAST_ADDR;
    
    public RCNetMulticastServerTH(final String multicastAddr, final int port) {
        this.socket = null;
        this.packet = null;
        this.channel = null;
        this.address = null;
        this.DEFAULT_MULTICAST_PORT = -1;
        this.DEFAULT_MULTICAST_ADDR = null;
        try {
            this.DEFAULT_MULTICAST_ADDR = multicastAddr;
            this.DEFAULT_MULTICAST_PORT = port;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public boolean init() {
        boolean result = false;
        try {
            this.socket = new DatagramSocket(this.DEFAULT_MULTICAST_PORT);
            this.channel = DatagramChannel.open().bind(null);
            this.address = InetAddress.getByName(this.DEFAULT_MULTICAST_ADDR);
            if (this.address != null) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            System.out.println("Init >> " + result);
        }
    }
    
    public boolean send(final ByteBuffer message) {
        boolean result = false;
        try {
            if (this.channel.send(message, new InetSocketAddress(this.DEFAULT_MULTICAST_ADDR, this.DEFAULT_MULTICAST_PORT)) >= 0) {
                result = true;
            }
            return result;
        }
        catch (ClosedChannelException cce) {
            Logger.error(cce);
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public void release() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.address = null;
            this.socket = null;
            System.out.println("Debug >> release ");
        }
    }
    
    public static void main(final String[] args) throws IOException {
        try {
            final RCNetMulticastServerTH m = new RCNetMulticastServerTH("230.0.0.1", 1235);
            m.startup("");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
