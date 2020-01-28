

package kr.com.illootech.framework.network.util;

import kr.com.illootech.framework.file.log.Logger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteOrderConverter
{
    public static ByteOrder getByteOrder(final byte[] data, final int offset, final int len) {
        try {
            final ByteBuffer buf = ByteBuffer.wrap(data);
            if (buf != null) {
                return buf.order();
            }
            return null;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static byte[] convertTo4ByteLittleEndian(final int data) {
        final byte[] buf = new byte[4];
        try {
            buf[3] = (byte)(data >>> 24 & 0xFF);
            buf[2] = (byte)(data >>> 16 & 0xFF);
            buf[1] = (byte)(data >>> 8 & 0xFF);
            buf[0] = (byte)(data >>> 0 & 0xFF);
            return buf;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static int convertTo4ByteBigEndianInteger(final byte[] data) {
        final int[] arr = new int[4];
        try {
            for (int i = 0; i < 4; ++i) {
                arr[i] = (data[3 - i] & 0xFF);
            }
            return (arr[0] << 24) + (arr[1] << 16) + (arr[2] << 8) + (arr[3] << 0);
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
}
