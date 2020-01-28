

package kr.com.illootech.framework.utils.operator;

import kr.com.illootech.framework.file.log.Logger;
import java.nio.ByteBuffer;

public class ShortOperator
{
    public static byte[] ShortToByte(final short data) {
        try {
            final byte[] tmpbyte = new byte[2];
            final ByteBuffer sb = ByteBuffer.wrap(tmpbyte);
            sb.putShort(data);
            return tmpbyte;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
}
