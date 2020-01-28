

package kr.com.illootech.framework.crypto.common;

public interface CryptoPadding
{
    byte[] addPadding(final byte[] p0, final int p1);
    
    byte[] removePadding(final byte[] p0, final int p1);
}
