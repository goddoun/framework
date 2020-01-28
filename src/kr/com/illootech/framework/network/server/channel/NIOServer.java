

package kr.com.illootech.framework.network.server.channel;

import kr.com.illootech.framework.network.server.channel.session.SessionDataset;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectableChannel;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import kr.com.illootech.framework.file.log.Logger;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import kr.com.illootech.framework.network.server.channel.session.SessionMessageQueue;
import kr.com.illootech.framework.thread.RCThread;

public class NIOServer extends RCThread
{
    private int DEFAULT_PORT;
    private SessionMessageQueue queue;
    private Selector selector;
    private ServerSocketChannel serverSocketCh;
    
    public NIOServer() {
        this.DEFAULT_PORT = 9000;
        this.queue = null;
        this.selector = null;
        this.serverSocketCh = null;
    }
    
    protected boolean createServerChannel(final SessionMessageQueue queue, final int port) {
        final boolean result = false;
        try {
            this.DEFAULT_PORT = port;
            this.queue = queue;
            this.selector = Selector.open();
            this.serverSocketCh = ServerSocketChannel.open();
            if (!this.selector.isOpen() || !this.serverSocketCh.isOpen()) {
                return result;
            }
            this.serverSocketCh.configureBlocking(false);
            this.serverSocketCh.setOption(StandardSocketOptions.SO_RCVBUF, 2560);
            this.serverSocketCh.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            this.serverSocketCh.bind(new InetSocketAddress(this.DEFAULT_PORT));
            this.serverSocketCh.register(this.selector, 16);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    protected void destoryServerChannel() {
        try {
            if (this.selector != null) {
                this.selector.close();
            }
            if (this.serverSocketCh != null) {
                this.serverSocketCh.close();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
        finally {
            this.selector = null;
            this.serverSocketCh = null;
        }
    }
    
    protected boolean processing() {
        final boolean result = false;
        final int DEFAULT_SELECTOR_BLTO = 1000;
        try {
            System.out.println("[server] waiting for connections......");
            try {
                if (!this.selector.isOpen()) {
                    System.out.println("[server] selector is closed");
                    return result;
                }
                final int eventCount = this.selector.select(DEFAULT_SELECTOR_BLTO);
                System.out.println("[server] '" + eventCount + "' selected from server socket channel......");
                Thread.sleep(1000L);
                if (eventCount > 0) {
                    final Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        System.out.println("[server] selectionKeys has next");
                        final SelectionKey key = keys.next();
                        if (key == null || !key.isValid()) {
                            System.out.println("[server] Invalid 'SelectionKey'");
                            keys.remove();
                        }
                        else {
                            if (this.isAccept(key.channel())) {
                                this.processingAccept(key, key.channel());
                            }
                            else {
                                this.putSessionDatasetToMessageQ(key);
                            }
                            keys.remove();
                            System.out.println("[server] remove last selection.key");
                        }
                    }
                    System.out.println("[server] escape this second while loop\n");
                }
                return result;
            }
            catch (Exception e) {
                Logger.error(e);
                return false;
            }
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            this.destoryServerChannel();
        }
    }
    
    protected boolean isAccept(final SelectableChannel channel) {
        boolean result = false;
        try {
            if (channel instanceof ServerSocketChannel) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    protected boolean processingAccept(final SelectionKey key, final SelectableChannel channel) {
        boolean result = false;
        try {
            final ServerSocketChannel serverChannel = (ServerSocketChannel)channel;
            final SocketChannel socketChannel = serverChannel.accept();
            if (socketChannel == null) {
                System.out.println("## null server socket");
                return result;
            }
            System.out.println("[" + this.getName() + "] ## socket accepted : " + socketChannel);
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), this.getSocketOption());
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    protected int getSocketOption() {
        return 13;
    }
    
    private boolean putSessionDatasetToMessageQ(final SelectionKey key) {
        boolean result = false;
        SessionDataset sessionDataset = null;
        try {
            sessionDataset = new SessionDataset();
            sessionDataset.set(key);
            System.out.println("[server] created session.data instance with 'SelectionKey'");
            this.queue.put(sessionDataset);
            System.out.println("[server] push session.data to messageQ, size is '" + this.queue.size() + "'");
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
}
