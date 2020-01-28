

package kr.com.illootech.framework.network.client;

import java.nio.ByteBuffer;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;
import kr.com.illootech.framework.file.log.Logger;
import java.nio.channels.MembershipKey;
import java.nio.channels.DatagramChannel;
import java.net.InetAddress;
import java.net.NetworkInterface;
import kr.com.illootech.framework.thread.IFRCThread;
import kr.com.illootech.framework.thread.RCThread;

public class RCNetMulticastClientTH extends RCThread implements IFRCThread
{
    public NetworkInterface networkInterface;
    public InetAddress group;
    public DatagramChannel client;
    public MembershipKey key;
    public String DEFAULT_ETHERNETCARD_NAME;
    public String DEFAULT_JOIN_GRP_ADDRESS;
    public int DEFAULT_JOIN_GRP_PORT;
    
    public RCNetMulticastClientTH() {
        this.networkInterface = null;
        this.group = null;
        this.client = null;
        this.key = null;
        this.DEFAULT_ETHERNETCARD_NAME = null;
        this.DEFAULT_JOIN_GRP_ADDRESS = "239.2.37.221";
        this.DEFAULT_JOIN_GRP_PORT = 2021;
    }
    
    public boolean init(final String ethernetCardName, final String groupIp, final int groupPort) {
        boolean result = false;
        try {
            System.out.println("0");
            if (ethernetCardName == null || groupIp == null || groupPort < 0) {
                return result;
            }
            System.out.println("1");
            this.DEFAULT_ETHERNETCARD_NAME = ethernetCardName;
            this.DEFAULT_JOIN_GRP_ADDRESS = groupIp;
            this.DEFAULT_JOIN_GRP_PORT = groupPort;
            this.networkInterface = NetworkInterface.getByName(ethernetCardName);
            this.group = InetAddress.getByName(this.DEFAULT_JOIN_GRP_ADDRESS);
            System.out.println("2");
            result = true;
            return result;
        }
        catch (Exception e) {
            System.out.println("Debug >> " + e.getMessage());
            e.printStackTrace();
            Logger.error(e);
            return false;
        }
    }
    
    public void release() {
        try {
            if (this.key != null) {
                this.key.drop();
            }
            if (this.client != null) {
                this.client.disconnect();
            }
        }
        catch (Exception e) {
            System.out.println("Debug >> " + e.getMessage());
            e.printStackTrace();
            Logger.error(e);
        }
        finally {
            this.key = null;
            this.client = null;
        }
    }
    
    public boolean joinGroup() {
        final boolean result = false;
        try {
            (this.client = DatagramChannel.open(StandardProtocolFamily.INET)).setOption(StandardSocketOptions.SO_REUSEADDR, true);
            this.client.bind(new InetSocketAddress(this.DEFAULT_JOIN_GRP_PORT));
            this.client.setOption(StandardSocketOptions.IP_MULTICAST_IF, this.networkInterface);
            this.key = this.client.join(this.group, this.networkInterface);
            return result;
        }
        catch (Exception e) {
            System.out.println("Debug >> " + e.getMessage());
            e.printStackTrace();
            Logger.error(e);
            return false;
        }
    }
    
    public static void main(final String[] args) {
        try {
            System.out.println("ddd-0---");
            final RCNetMulticastClientTH multi = new RCNetMulticastClientTH();
            System.out.println("ddd");
            multi.init("en1", "239.2.37.221", 12345);
            multi.joinGroup();
            final ByteBuffer packet = ByteBuffer.allocate(100);
            for (int i = 0; i < 10; ++i) {
                multi.client.receive(packet);
                final String notice = new String(packet.array());
                System.out.println(notice);
            }
            multi.client.disconnect();
        }
        catch (Exception e) {
            System.out.println("Debug >> " + e.getMessage());
            e.printStackTrace();
        }
    }
}
