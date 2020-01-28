

package kr.com.illootech.framework.crypto.seed.ansiX923;

public interface CryptoPadding
{
    byte[] addPadding(final byte[] p0, final int p1);
    
    byte[] removePadding(final byte[] p0, final int p1);
}
