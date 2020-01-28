

package kr.com.illootech.framework.network.server.channel;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.nio.channels.SocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.io.IOException;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import kr.com.illootech.framework.thread.RCThread;

public abstract class RCNetServerCH extends RCThread
{
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket;
    private int DEFAULT_SOCKET_BUF_SIZE;
    
    public RCNetServerCH() {
        this.selector = null;
        this.serverSocketChannel = null;
        this.serverSocket = null;
        this.DEFAULT_SOCKET_BUF_SIZE = 2048;
    }
    
    public void initServer(final String ip, final int port) {
        try {
            this.selector = Selector.open();
            (this.serverSocketChannel = ServerSocketChannel.open()).configureBlocking(false);
            this.serverSocket = this.serverSocketChannel.socket();
            final InetSocketAddress isa = new InetSocketAddress(ip, port);
            this.serverSocket.bind(isa);
            this.serverSocket.setReuseAddress(true);
            this.serverSocketChannel.register(this.selector, 16);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Init][Server channel & binding] - Success", " ", "Framework"), LoggerElements.LOG_LEVEL3);
        }
        catch (IOException ie) {
            Logger.error(ie);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Init][Server channel & binding] - Fail", " ", "Framework"), LoggerElements.LOG_LEVEL3);
        }
        catch (Exception e) {
            Logger.error(e);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Init][Server channel & binding] - Fail", " ", "Framework"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public void initServer(final String ip, final int port, final int defaultSockBufferSize) {
        try {
            this.selector = Selector.open();
            (this.serverSocketChannel = ServerSocketChannel.open()).configureBlocking(false);
            (this.serverSocket = this.serverSocketChannel.socket()).setReuseAddress(true);
            this.DEFAULT_SOCKET_BUF_SIZE = defaultSockBufferSize;
            final InetSocketAddress isa = new InetSocketAddress(ip, port);
            this.serverSocket.bind(isa);
            this.serverSocketChannel.register(this.selector, 16);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Init][Server channel & binding] - Success", " ", "Framework"), LoggerElements.LOG_LEVEL3);
        }
        catch (IOException ie) {
            Logger.error(ie);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Init][Server channel & binding] - Fail", " ", "Framework"), LoggerElements.LOG_LEVEL3);
        }
        catch (Exception e) {
            Logger.error(e);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Init][Server channel & binding] - Fail", " ", "Framework"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public void Accept(final SelectionKey key) {
        final ServerSocketChannel server = (ServerSocketChannel)key.channel();
        SocketChannel sc = null;
        try {
            sc = server.accept();
            final String address = sc.socket().getInetAddress().toString();
            this.RegisterChannel(this.selector, sc, 5);
            Logger.sysInfo(String.format("[%1$-20s] [%2$-10s][Connect - allowed][%3$s] - Success", " ", "Framework", address), LoggerElements.LOG_LEVEL3);
        }
        catch (ClosedChannelException e) {
            Logger.error(e);
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e2) {
            Logger.error(e2);
        }
    }
    
    public void RegisterChannel(final Selector selector, final SocketChannel sc, final int ops) {
        try {
            if (sc == null) {
                return;
            }
            sc.socket().setSendBufferSize(this.DEFAULT_SOCKET_BUF_SIZE);
            sc.socket().setReceiveBufferSize(this.DEFAULT_SOCKET_BUF_SIZE);
            sc.configureBlocking(false);
            sc.register(selector, ops);
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public Iterator<SelectionKey> GetChannelSelect() {
        try {
            this.selector.select();
            final Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
            return it;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public boolean SendTo(final SelectionKey key, final byte[] data) {
        SocketChannel sc = null;
        try {
            final ByteBuffer buf = ByteBuffer.wrap(data);
            sc = (SocketChannel)key.channel();
            sc.write(buf);
            buf.rewind();
            return true;
        }
        catch (IOException ie) {
            this.ReleaseClientResource(key, sc);
            Logger.error(ie);
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean SendTo(final SelectionKey key, final ByteBuffer data) {
        SocketChannel sc = null;
        try {
            sc = (SocketChannel)key.channel();
            data.rewind();
            sc.write(data);
            return true;
        }
        catch (IOException ie) {
            this.ReleaseClientResource(key, sc);
            Logger.error(ie);
            return false;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public abstract ByteBuffer ReadFrom(final SelectionKey p0);
    
    public void clearBuffer(ByteBuffer buffer) {
        try {
            if (buffer != null) {
                buffer.clear();
            }
            buffer = null;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void ReleaseClientResource(final SelectionKey key, final SocketChannel sc) {
        try {
            if (sc != null) {
                sc.close();
            }
            if (key != null) {
                key.cancel();
            }
        }
        catch (IOException ie) {
            Logger.error(ie);
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public void ReleaseResource() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            if (this.serverSocketChannel != null) {
                this.serverSocketChannel.close();
            }
            if (this.selector != null) {
                this.selector.close();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.serverSocket = null;
            this.serverSocketChannel = null;
            this.selector = null;
        }
    }
}
