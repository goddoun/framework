

package kr.com.illootech.framework.utils.operator;

import java.nio.ByteBuffer;

import kr.com.illootech.framework.file.log.Logger;

public class ByteOperator
{
    public static short TwoByteToShort(final byte[] data) {
        try {
            final ByteBuffer buf = ByteBuffer.wrap(data);
            return buf.getShort();
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public static int FourByteToInt(final byte[] data) {
        try {
            final ByteBuffer buf = ByteBuffer.wrap(data);
            return buf.getInt();
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public static int FourByteToInt(final byte[] data, final int offset, final int length) {
        try {
            final ByteBuffer buf = ByteBuffer.wrap(data, offset, length);
            return buf.getInt();
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public static String ByteToHexStr(final byte[] data, final int offset, final int length) {
        try {
            return ByteToHexStr(data, offset, length, "");
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static String ByteToHexStr(final byte[] data, final int offset, int length, final String delimeter) {
        try {
            final StringBuffer tmpSB = new StringBuffer();
            length += offset;
            for (int i = offset; i < length; ++i) {
                if (data[i] >= 0 && data[i] <= 15) {
                    tmpSB.append("0");
                }
                tmpSB.append(Integer.toHexString(data[i] & 0xFF));
                tmpSB.append(delimeter);
            }
            return tmpSB.toString();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static String ByteToBitStr(byte data, final String bitDelimiter) {
        final int bitCnt = 8;
        final byte opUnit = 1;
        StringBuffer result = null;
        try {
            result = new StringBuffer();
            for (int i = 0; i < 8; ++i) {
                result.append(Integer.toHexString(data & 0x1));
                data >>= 1;
                if (i == 3 && bitDelimiter != null) {
                    result.append(bitDelimiter);
                }
            }
            result.reverse();
            return result.toString();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static String[] ByteArrToBitStr(final byte[] data, final String bitDelimiter) {
        String[] result = null;
        int size = 0;
        try {
            if (data == null) {
                return result;
            }
            size = data.length;
            result = new String[size];
            for (int i = 0; i < size; ++i) {
                final String tmpStr = ByteToBitStr(data[i], bitDelimiter);
                if (tmpStr == null) {
                    return result;
                }
                result[i] = tmpStr;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static byte[] divideByteData(final byte input) {
        byte[] data = null;
        try {
            data = new byte[0];
            data[0] = input;
            return divideByteData(data);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] divideByteData(final byte[] input) {
        byte[] retByte = null;
        try {
            int inputByteDataLen = 0;
            final byte[] tmpByte = new byte[2];
            inputByteDataLen = input.length;
            retByte = new byte[inputByteDataLen * 2];
            int i = 0;
            int j = 0;
            while (i < inputByteDataLen) {
                tmpByte[0] = (byte)(input[i] >> 4 & 0xF);
                retByte[j++] = tmpByte[0];
                tmpByte[1] = (byte)(input[i] & 0xF);
                retByte[j++] = tmpByte[1];
                ++i;
            }
            return retByte;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(final String[] args) {
        final int arraySize = 256;
        byte tmpByte = 0;
        final byte[] tmpByteArr = new byte[256];
        for (int i = 0; i < 256; ++i) {
            final byte[] array = tmpByteArr;
            final int n = i;
            final byte b = tmpByte;
            tmpByte = (byte)(b + 1);
            array[n] = b;
        }
        final String[] tmpStrArr = ByteArrToBitStr(tmpByteArr, " ");
        for (int j = 0; j < 256; ++j) {
            System.out.println(tmpStrArr[j]);
        }
    }
}
