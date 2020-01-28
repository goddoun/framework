

package kr.com.illootech.framework.utils.operator;

import kr.com.illootech.framework.file.log.Logger;

public class IntegerOperator
{
    public static String IntToStr(final int data) {
        try {
            return String.valueOf(data);
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static byte[] Int2ByteArray(final int num) {
        try {
            final byte[] b = { (byte)(num & 0xFF), (byte)(num >> 8 & 0xFF), (byte)(num >> 16 & 0xFF), (byte)(num >> 24 & 0xFF) };
            return b;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
