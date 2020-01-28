

package kr.com.illootech.framework.network.protocol.websocket;

import kr.com.illootech.framework.utils.operator.ByteOperator;
import kr.com.illootech.framework.file.log.Logger;

public class WebSocketProtocolSet
{
    private boolean FIN;
    private byte RESERVED;
    private byte OPCODE;
    private boolean MASK_YN;
    private int DATALEN;
    private byte[] MASK_KEY;
    private byte[] Header;
    private byte[] Body;
    
    public WebSocketProtocolSet() {
        this.FIN = false;
        this.RESERVED = 0;
        this.OPCODE = 0;
        this.MASK_YN = false;
        this.DATALEN = 0;
        this.MASK_KEY = null;
        this.Header = null;
        this.Body = null;
    }
    
    public boolean setHeader(final byte[] packet) {
        boolean result = false;
        try {
            this.Header = packet;
            if (!this.setFIN(packet[0])) {
                return result;
            }
            if (!this.setOPCode(packet[0])) {
                return result;
            }
            if (!this.setMaskYn(packet[1])) {
                return result;
            }
            if (!this.setDataLen(packet[1])) {
                return result;
            }
            if (!this.setMaskingKey(packet)) {
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
    
    public boolean setFIN(final byte data) {
        boolean result = false;
        String bitStr = null;
        try {
            bitStr = ByteOperator.ByteToBitStr(data, "");
            if (bitStr == null) {
                return result;
            }
            if (bitStr.startsWith("1")) {
                this.FIN = true;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean setOPCode(final byte data) {
        boolean result = false;
        try {
            this.OPCODE = (byte)(data & 0xF);
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean setMaskYn(byte data) {
        boolean result = false;
        try {
            data = (byte)((data & 0x80) >> 7);
            if (data == 1) {
                this.MASK_YN = true;
            }
            else {
                this.MASK_YN = false;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean setDataLen(byte data) {
        boolean result = false;
        try {
            data &= 0x7F;
            this.DATALEN = data;
            if (this.DATALEN < 0) {
                this.DATALEN += 256;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean setDataLen(final byte[] data) {
        boolean result = false;
        int dataLen = 0;
        try {
            dataLen = data.length;
            switch (dataLen) {
                case 2: {
                    this.DATALEN = ByteOperator.TwoByteToShort(data);
                    break;
                }
                case 8: {
                    this.DATALEN = ByteOperator.FourByteToInt(data);
                    break;
                }
                default: {
                    return result;
                }
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean setMaskingKey(final byte[] data) {
        boolean result = false;
        try {
            if (data == null) {
                return result;
            }
            if (this.MASK_KEY == null) {
                this.MASK_KEY = new byte[4];
            }
            System.arraycopy(data, 0, this.MASK_KEY, 0, 4);
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean setData(final byte[] data) {
        boolean result = false;
        try {
            if (data == null) {
                return result;
            }
            if (this.MASK_YN) {
                this.Body = new byte[this.DATALEN];
                for (int i = 0; i < this.DATALEN; ++i) {
                    this.Body[i] = (byte)(data[i] ^ this.MASK_KEY[i % 4]);
                }
            }
            else {
                this.Body = data;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    
    public boolean getFIN() {
        return this.FIN;
    }
    
    public byte getReservedField() {
        return this.RESERVED;
    }
    
    public byte getOPCode() {
        return this.OPCODE;
    }
    
    public boolean getMaskYn() {
        return this.MASK_YN;
    }
    
    public byte[] getMaskingKey() {
        return this.MASK_KEY;
    }
    
    public int getDataLen() {
        return this.DATALEN;
    }
    
    public byte[] getHeader() {
        return this.Header;
    }
    
    public byte[] getData() {
        return this.Body;
    }
}
