

package kr.com.illootech.framework.crypto.common;

public class AnsiX923Padding implements CryptoPadding
{
    private String name;
    private final byte PADDING_VALUE = 0;
    
    public AnsiX923Padding() {
        this.name = "ANSI-X.923-Padding";
    }
    
    @Override
    public byte[] addPadding(final byte[] source, final int blockSize) {
        int paddingCnt = -1;
        byte[] paddingResult = null;
        try {
            paddingCnt = source.length % blockSize;
            paddingResult = null;
            if (paddingCnt != 0) {
                paddingResult = new byte[source.length + (blockSize - paddingCnt)];
                System.arraycopy(source, 0, paddingResult, 0, source.length);
                final int addPaddingCnt = blockSize - paddingCnt;
                for (int i = 0; i < addPaddingCnt; ++i) {
                    paddingResult[source.length + i] = 0;
                }
                paddingResult[paddingResult.length - 1] = (byte)addPaddingCnt;
            }
            else {
                final byte[] tmpPaddingData = new byte[blockSize];
                for (int i = 0; i < blockSize; ++i) {
                    if (i == blockSize - 1) {
                        tmpPaddingData[i] = (byte)blockSize;
                    }
                    else {
                        tmpPaddingData[i] = 0;
                    }
                }
                paddingResult = new byte[source.length + tmpPaddingData.length];
                System.arraycopy(source, 0, paddingResult, 0, source.length);
                System.arraycopy(tmpPaddingData, 0, paddingResult, source.length, tmpPaddingData.length);
            }
            return paddingResult;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public byte[] removePadding(final byte[] source, final int blockSize) {
        byte[] paddingResult = null;
        boolean isPadding = true;
        try {
            int lastValue = source[source.length - 1];
            if (lastValue < 0) {
                lastValue += 256;
            }
            for (int limit = source.length - lastValue, i = source.length - 2; i >= limit; --i) {
                if (source[i] != 0) {
                    isPadding = false;
                    break;
                }
            }
            if (isPadding) {
                paddingResult = new byte[source.length - lastValue];
                System.arraycopy(source, 0, paddingResult, 0, paddingResult.length);
            }
            else {
                paddingResult = source;
            }
            return paddingResult;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String getName() {
        return this.name;
    }
}
