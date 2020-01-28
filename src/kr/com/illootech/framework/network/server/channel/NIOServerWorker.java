

package kr.com.illootech.framework.network.server.channel;

import java.nio.CharBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.network.server.channel.session.SessionMessageQueue;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import kr.com.illootech.framework.network.server.channel.session.SessionDataset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import kr.com.illootech.framework.thread.RCThread;

public class NIOServerWorker extends RCThread
{
    private final String DEFAULT_CHARSET = "UTF-8";
    private Charset charset;
    private CharsetEncoder encoder;
    private SessionDataset sessionDataset;
    private SelectionKey selectionKey;
    private SelectableChannel selectableChannel;
    private SocketChannel socketChannel;
    private SessionMessageQueue inQueue;
    
    public NIOServerWorker(final SessionMessageQueue queue) {
        this.charset = Charset.forName("UTF-8");
        this.encoder = this.charset.newEncoder();
        this.sessionDataset = null;
        this.selectionKey = null;
        this.selectableChannel = null;
        this.socketChannel = null;
        this.inQueue = null;
        this.inQueue = queue;
    }
    
    private SessionDataset getSessionDataset() {
        SessionDataset result = null;
        try {
            if (this.inQueue == null || this.inQueue.size() <= 0L) {
                return result;
            }
            result = this.inQueue.get();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    private SelectionKey getSelectionKey(final SessionDataset dataset) {
        SelectionKey result = null;
        try {
            if (dataset == null) {
                return result;
            }
            result = dataset.getKey();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    private SelectableChannel getChannel(final SessionDataset dataset) {
        SelectionKey key = null;
        SelectableChannel result = null;
        try {
            if (dataset == null) {
                return result;
            }
            key = dataset.getKey();
            if (key == null) {
                return result;
            }
            result = key.channel();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    private boolean isConnectable() {
        boolean result = false;
        try {
            if (this.socketChannel == null) {
                return result;
            }
            if (!this.socketChannel.isConnectionPending()) {
                result = true;
            }
            else {
                System.out.println("Client socket.channel is connection pending - closed");
                this.socketChannel.finishConnect();
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    private boolean isReadable() {
        boolean result = false;
        try {
            if (this.selectionKey == null) {
                return result;
            }
            if (this.selectionKey.isReadable()) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    protected ByteBuffer readFrom(final int size) {
        ByteBuffer result = null;
        try {
            if (this.socketChannel == null || size < 1 || !this.isConnectable()) {
                return result;
            }
            result = ByteBuffer.allocate(size);
            this.socketChannel.read(result);
            if (result.position() != 0) {
                System.out.print("[" + this.getName() + "] : ");
                while (result.hasRemaining()) {
                    System.out.print((char)result.get());
                }
            }
            result.rewind();
            return result;
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    private boolean isWritable() {
        boolean result = false;
        try {
            if (this.selectionKey == null) {
                return result;
            }
            if (this.selectionKey.isWritable()) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    protected boolean sendTo(final ByteBuffer data, final int size) {
        final boolean result = false;
        try {
            if (this.socketChannel == null || data == null || size < 1) {
                return result;
            }
            this.socketChannel.write(data);
            final String str = "[" + this.getName() + "] \ufffd\ufffd\ub2ff\uad54 \ufffd\ufffd\ufffd\u8e30\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \u8e42\ub300\ufffd\ufffd \ufffd\ufffd\uacd7\ufffd\ub304\ufffd\ufffd...222222222";
            this.socketChannel.write(this.encoder.encode(CharBuffer.wrap(new StringBuilder(String.valueOf(str)).toString())));
            System.out.println("\ufffd\ufffd\ufffd\u8e30\ufffd\u5a9b\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\u044b\ufffd\ufffd \ufffd\ufffd\ub301\ufffd\ufffd : " + str);
            return result;
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
    
    protected boolean init() {
        boolean result = false;
        try {
            this.sessionDataset = this.getSessionDataset();
            if (this.sessionDataset == null) {
                return result;
            }
            this.selectionKey = this.getSelectionKey(this.sessionDataset);
            if (this.selectionKey == null) {
                return result;
            }
            this.selectableChannel = this.getChannel(this.sessionDataset);
            if (this.selectableChannel == null) {
                return result;
            }
            this.socketChannel = (SocketChannel)this.selectableChannel;
            if (this.socketChannel == null) {
                return result;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    protected void reset() {
        try {
            this.socketChannel = null;
            this.selectableChannel = null;
            this.selectionKey = null;
            this.sessionDataset = null;
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    protected void sample() {
        ByteBuffer buff = null;
        try {
            while (this.getLoopAllowFlag()) {
                SessionDataset session = null;
                session = this.getSessionDataset();
                if (session == null) {
                    continue;
                }
                final SelectionKey selected = this.getSelectionKey(session);
                if (selected == null) {
                    continue;
                }
                final SelectableChannel channel = this.getChannel(session);
                final SocketChannel socketChannel = (SocketChannel)channel;
                buff = ByteBuffer.allocate(100);
                if (this.isConnectable()) {
                    if (this.isReadable()) {
                        try {
                            socketChannel.read(buff);
                            if (buff.position() != 0) {
                                buff.clear();
                                System.out.print("[" + this.getName() + "] \ufffd\ufffd\ub300\ufffd\uc1f1\ufffd\ub301\ufffd\uba85\ufffd\uba83\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\u0449\ufffd\ufffd \ufffd\ufffd\ub301\ufffd\ufffd : ");
                                while (buff.hasRemaining()) {
                                    System.out.print((char)buff.get());
                                }
                                buff.clear();
                                System.out.println();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            socketChannel.finishConnect();
                            socketChannel.close();
                        }
                    }
                    if (this.isWritable()) {
                        final String str = "[" + this.getName() + "] \ufffd\ufffd\ub2ff\uad54 \ufffd\ufffd\ufffd\u8e30\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \u8e42\ub300\ufffd\ufffd \ufffd\ufffd\uacd7\ufffd\ub304\ufffd\ufffd...222222222";
                        socketChannel.write(this.encoder.encode(CharBuffer.wrap(new StringBuilder(String.valueOf(str)).toString())));
                        System.out.println("\ufffd\ufffd\ufffd\u8e30\ufffd\u5a9b\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\u044b\ufffd\ufffd \ufffd\ufffd\ub301\ufffd\ufffd : " + str);
                    }
                }
                this.delay(100L);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
