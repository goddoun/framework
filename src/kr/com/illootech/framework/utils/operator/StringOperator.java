

package kr.com.illootech.framework.utils.operator;

import kr.com.illootech.framework.file.log.Logger;

public class StringOperator
{
    public static int StrToInt(final String data) {
        try {
            return Integer.valueOf(data);
        }
        catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }
    
    public static byte[] StringToASCIIHex(final String data) {
        byte[] source = null;
        byte[] result = null;
        int sourceLen = 0;
        try {
            source = data.getBytes();
            sourceLen = source.length;
            if (sourceLen < 2) {
                return data.getBytes();
            }
            result = new byte[sourceLen / 2];
            for (int i = 0; i < sourceLen; ++i) {
                if (i % 2 != 0) {
                    if ((byte)(source[i] & 0x30) == 48) {
                        final byte[] array = result;
                        final int n = i / 2;
                        array[n] |= (byte)(source[i] & 0xF);
                    }
                    else if ((byte)(source[i] & 0x40) == 64 || (byte)(source[i] & 0x60) == 96) {
                        final byte[] array2 = result;
                        final int n2 = i / 2;
                        array2[n2] |= (byte)((source[i] & 0xF) + 9);
                    }
                    else {
                        final byte[] array3 = result;
                        final int n3 = i / 2;
                        array3[n3] |= 0x0;
                    }
                }
                else if ((byte)(source[i] & 0x30) == 48) {
                    result[i / 2] = (byte)((source[i] & 0xF) << 4);
                }
                else if ((byte)(source[i] & 0x40) == 64 || (byte)(source[i] & 0x60) == 96) {
                    result[i / 2] = (byte)((source[i] & 0xF) + 9 << 4);
                }
                else {
                    result[i / 2] = 0;
                }
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
