

package kr.com.illootech.framework.network.protocol.websocket;

import kr.com.illootech.framework.crypto.encoding.BASE64;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.util.HashMap;
import kr.com.illootech.framework.file.log.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import kr.com.illootech.framework.crypto.hash.SHA;
import kr.com.illootech.framework.network.util.io.NetIOHandler;

public class WebSocketHandler
{
    public final int defaultPrefixLen = 2;
    private NetIOHandler netIOHandler;
    private final String defaultServerKey = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private SHA shadowBox;
    
    public WebSocketHandler() {
        this.netIOHandler = null;
        this.shadowBox = new SHA();
        this.netIOHandler = new NetIOHandler();
        this.shadowBox = new SHA();
    }
    
    public boolean handshaking(final BufferedInputStream in, final BufferedOutputStream out, final int sockCheckDelay, final int sockTimeout, int dataSize) {
        boolean result = false;
        byte[] tmpByte = null;
        StringBuffer sbuf = null;
        HashMap<String, String> map = null;
        String response = null;
        try {
            sbuf = new StringBuffer();
            while ((dataSize = in.available()) > 0) {
                tmpByte = this.netIOHandler.readFrom(in, sockCheckDelay, sockTimeout, dataSize);
                sbuf.append(new String(tmpByte));
            }
            if (!this.isHandShaking(sbuf.toString())) {
                return result;
            }
            map = this.parsingHandShakingElements(sbuf.toString());
            if (map == null) {
                return result;
            }
            response = this.makeResponseForHandShaking(map);
            if (response == null) {
                return result;
            }
            if (out == null) {
                return result;
            }
            out.write(response.getBytes());
            out.flush();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    private boolean isHandShaking(final String packet) {
        final String prefixRQHeader = "GET";
        final String prefixRSHeader = "HTTP";
        boolean result = false;
        try {
            if (packet == null) {
                return result;
            }
            if (packet.startsWith("GET") || packet.startsWith("HTTP")) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    private HashMap<String, String> parsingHandShakingElements(final String packet) {
        final String[] tags = { "Upgrade", "Connection", "Host", "Origin", "Pragma", "Cache-Control", "Sec-WebSocket-Key", "Sec-WebSocket-Version", "Sec-WebSocket-Extensions", "User-Agent" };
        int tagsCnt = 0;
        HashMap<String, String> result = null;
        String tmpStr = null;
        try {
            if (packet == null) {
                return result;
            }
            tagsCnt = tags.length;
            if (tagsCnt <= 0) {
                return result;
            }
            result = new HashMap<String, String>();
            int loopCnt = 0;
            int startPos = 0;
            int endPos = 0;
            while (loopCnt < tagsCnt) {
                startPos = packet.indexOf(tags[loopCnt]);
                if (startPos >= 0) {
                    endPos = packet.indexOf("\n", startPos);
                    if (endPos < 0) {
                        endPos = packet.indexOf(tags[loopCnt + 1], startPos);
                    }
                    startPos += tags[loopCnt].length();
                    tmpStr = packet.substring(startPos + 1, endPos).trim();
                    if (tmpStr == null) {
                        return result;
                    }
                    result.put(tags[loopCnt], tmpStr);
                    ++loopCnt;
                }
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$20s][%2$30s][FRAMEWORK][Parsing websocket.handshaking.elements %3$s]", "", "", result), LoggerElements.LOG_LEVEL1);
        }
    }
    
    private String makeResponseForHandShaking(final HashMap<String, String> elements) {
        StringBuffer result = null;
        try {
            result = new StringBuffer();
            result.append("HTTP/1.1 101 Switching Protocols\r\n");
            result.append("Upgrade: ");
            result.append(elements.get("Upgrade"));
            result.append("\r\n");
            result.append("Connection: ");
            result.append(elements.get("Connection"));
            result.append("\r\n");
            String tmpStr = elements.get("Sec-WebSocket-Key");
            if (tmpStr == null) {
                return null;
            }
            tmpStr = String.valueOf(tmpStr) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            final byte[] resultForSHA1 = this.shadowBox.SHAWithByte("SHA-512", tmpStr.getBytes());
            if (resultForSHA1 == null) {
                return null;
            }
            tmpStr = null;
            tmpStr = BASE64.base64Encode(resultForSHA1);
            result.append("Sec-WebSocket-Accept: ");
            result.append(tmpStr);
            result.append("\r\n");
            result.append("Sec-WebSocket-Protocol: chat\r\n");
            result.append("Sec-WebSocket-Version: 13\r\n\r\n");
            return result.toString();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    private byte[] makeFrame(final byte[] data) {
        byte[] frame = null;
        int length = 0;
        int rawDataIndex = -1;
        try {
            if (data == null) {
                return null;
            }
            length = data.length;
            if (length <= 125) {
                rawDataIndex = 2;
            }
            else if (length >= 126 && length <= 65535) {
                rawDataIndex = 4;
            }
            else {
                rawDataIndex = 10;
            }
            frame = new byte[length + rawDataIndex];
            frame[0] = -127;
            if (rawDataIndex == 2) {
                frame[1] = (byte)length;
            }
            else if (rawDataIndex == 4) {
                frame[1] = 126;
                frame[2] = (byte)(length >> 8 & -1);
                frame[3] = (byte)(length & -1);
            }
            else {
                frame[1] = 127;
                frame[2] = (byte)(length >> 56 & -1);
                frame[3] = (byte)(length >> 48 & -1);
                frame[4] = (byte)(length >> 40 & -1);
                frame[5] = (byte)(length >> 32 & -1);
                frame[6] = (byte)(length >> 24 & -1);
                frame[7] = (byte)(length >> 16 & -1);
                frame[8] = (byte)(length >> 8 & -1);
                frame[9] = (byte)(length & -1);
            }
            System.arraycopy(data, 0, frame, rawDataIndex, length);
            return frame;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public boolean sendTo(final String data, final BufferedOutputStream out, final String charset) {
        boolean result = false;
        byte[] tmpByte = null;
        try {
            if (data == null || out == null) {
                return result;
            }
            tmpByte = data.getBytes(charset);
            result = this.sendTo(tmpByte, out);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean sendTo(final byte[] data, final BufferedOutputStream out) {
        boolean result = false;
        byte[] frame = null;
        try {
            frame = this.makeFrame(data);
            if (out == null) {
                return result;
            }
            out.write(frame);
            out.flush();
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public WebSocketProtocolSet recvFrom(final BufferedInputStream in, final int sockCheckDelay, final int sockTimeout) {
        byte[] tmpByte = null;
        WebSocketProtocolSet protocolSet = null;
        try {
            protocolSet = new WebSocketProtocolSet();
            tmpByte = this.netIOHandler.readFrom(in, sockCheckDelay, sockTimeout, 2);
            if (tmpByte == null) {
                return null;
            }
            protocolSet.setFIN(tmpByte[0]);
            protocolSet.setOPCode(tmpByte[1]);
            protocolSet.setMaskYn(tmpByte[1]);
            protocolSet.setDataLen(tmpByte[1]);
            final int dataLen = protocolSet.getDataLen();
            if (dataLen > 125) {
                if (dataLen == 126) {
                    tmpByte = null;
                    tmpByte = this.netIOHandler.readFrom(in, sockCheckDelay, sockTimeout, 2);
                    if (tmpByte == null) {
                        return null;
                    }
                    protocolSet.setDataLen(tmpByte);
                }
                else if (dataLen >= 127) {
                    tmpByte = null;
                    tmpByte = this.netIOHandler.readFrom(in, sockCheckDelay, sockTimeout, 8);
                    if (tmpByte == null) {
                        return null;
                    }
                    protocolSet.setDataLen(tmpByte);
                }
            }
            if (protocolSet.getMaskYn()) {
                tmpByte = null;
                tmpByte = this.netIOHandler.readFrom(in, sockCheckDelay, sockTimeout, 4);
                if (tmpByte == null) {
                    return null;
                }
                protocolSet.setMaskingKey(tmpByte);
            }
            tmpByte = null;
            tmpByte = this.netIOHandler.readFrom(in, sockCheckDelay, sockTimeout, protocolSet.getDataLen());
            if (tmpByte == null) {
                return null;
            }
            if (!protocolSet.setData(tmpByte)) {
                return null;
            }
            return protocolSet;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static void main(final String[] args) {
    }
}
