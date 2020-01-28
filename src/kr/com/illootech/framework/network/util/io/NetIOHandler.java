

package kr.com.illootech.framework.network.util.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedInputStream;

public class NetIOHandler
{
    public byte[] readFrom(final BufferedInputStream in, final int recvBufCheckDelay, final int timeout, final int len) {
        return this.readFrom("", in, recvBufCheckDelay, timeout, len);
    }
    
    public byte[] readFrom(final String procname, final BufferedInputStream in, final int recvBufCheckDelay, final int timeout, final int len) {
        byte[] packet = null;
        int bufferCheckCount = 0;
        try {
            if (in == null) {
                return null;
            }
            packet = new byte[len];
            while (in != null && in.available() < len) {
                if (bufferCheckCount == timeout) {
                    return null;
                }
                ++bufferCheckCount;
                try {
                    Thread.sleep(recvBufCheckDelay);
                }
                catch (InterruptedException ex) {}
            }
            in.read(packet, 0, len);
            return packet;
        }
        catch (IOException ie) {
            Logger.error(ie);
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            packet = null;
        }
    }
    
    public boolean writeTo(final BufferedOutputStream out, final byte[] data) {
        return this.writeTo("", out, data);
    }
    
    public boolean writeTo(final String procname, final BufferedOutputStream out, final byte[] data) {
        try {
            if (out != null) {
                out.write(data, 0, data.length);
                out.flush();
                return true;
            }
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
}
