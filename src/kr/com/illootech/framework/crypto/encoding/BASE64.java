

package kr.com.illootech.framework.crypto.encoding;

public class BASE64
{
    private static byte[] mBase64EncMap;
    private static byte[] mBase64DecMap;
    
    static {
        final byte[] base64Map = BASE64.mBase64EncMap = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        BASE64.mBase64DecMap = new byte[128];
        for (int i = 0; i < BASE64.mBase64EncMap.length; ++i) {
            BASE64.mBase64DecMap[BASE64.mBase64EncMap[i]] = (byte)i;
        }
    }
    
    public static String base64Encode(final byte[] aData) {
        if (aData == null || aData.length == 0) {
            throw new IllegalArgumentException("Can not encode NULL or empty byte array.");
        }
        final byte[] encodedBuf = new byte[(aData.length + 2) / 3 * 4];
        int srcIndex = 0;
        int destIndex = 0;
        while (srcIndex < aData.length - 2) {
            encodedBuf[destIndex++] = BASE64.mBase64EncMap[aData[srcIndex] >>> 2 & 0x3F];
            encodedBuf[destIndex++] = BASE64.mBase64EncMap[(aData[srcIndex + 1] >>> 4 & 0xF) | (aData[srcIndex] << 4 & 0x3F)];
            encodedBuf[destIndex++] = BASE64.mBase64EncMap[(aData[srcIndex + 2] >>> 6 & 0x3) | (aData[srcIndex + 1] << 2 & 0x3F)];
            encodedBuf[destIndex++] = BASE64.mBase64EncMap[aData[srcIndex + 2] & 0x3F];
            srcIndex += 3;
        }
        if (srcIndex < aData.length) {
            encodedBuf[destIndex++] = BASE64.mBase64EncMap[aData[srcIndex] >>> 2 & 0x3F];
            if (srcIndex < aData.length - 1) {
                encodedBuf[destIndex++] = BASE64.mBase64EncMap[(aData[srcIndex + 1] >>> 4 & 0xF) | (aData[srcIndex] << 4 & 0x3F)];
                encodedBuf[destIndex++] = BASE64.mBase64EncMap[aData[srcIndex + 1] << 2 & 0x3F];
            }
            else {
                encodedBuf[destIndex++] = BASE64.mBase64EncMap[aData[srcIndex] << 4 & 0x3F];
            }
        }
        while (destIndex < encodedBuf.length) {
            encodedBuf[destIndex] = 61;
            ++destIndex;
        }
        final String result = new String(encodedBuf);
        return result;
    }
    
    public static byte[] base64Decode(final String aData) {
        if (aData == null || aData.length() == 0) {
            throw new IllegalArgumentException("Can not decode NULL or empty string.");
        }
        byte[] data;
        int tail;
        for (data = aData.getBytes(), tail = data.length; data[tail - 1] == 61; --tail) {}
        final byte[] decodedBuf = new byte[tail - data.length / 4];
        for (int i = 0; i < data.length; ++i) {
            data[i] = BASE64.mBase64DecMap[data[i]];
        }
        int srcIndex = 0;
        int destIndex;
        for (destIndex = 0; destIndex < decodedBuf.length - 2; destIndex += 3) {
            decodedBuf[destIndex] = (byte)((data[srcIndex] << 2 & 0xFF) | (data[srcIndex + 1] >>> 4 & 0x3));
            decodedBuf[destIndex + 1] = (byte)((data[srcIndex + 1] << 4 & 0xFF) | (data[srcIndex + 2] >>> 2 & 0xF));
            decodedBuf[destIndex + 2] = (byte)((data[srcIndex + 2] << 6 & 0xFF) | (data[srcIndex + 3] & 0x3F));
            srcIndex += 4;
        }
        if (destIndex < decodedBuf.length) {
            decodedBuf[destIndex] = (byte)((data[srcIndex] << 2 & 0xFF) | (data[srcIndex + 1] >>> 4 & 0x3));
        }
        if (++destIndex < decodedBuf.length) {
            decodedBuf[destIndex] = (byte)((data[srcIndex + 1] << 4 & 0xFF) | (data[srcIndex + 2] >>> 2 & 0xF));
        }
        return decodedBuf;
    }
}
