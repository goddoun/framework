

package kr.com.illootech.framework.crypto.xor;

public class XOR
{
    private static final String DEFAULT_CHARSET = "UTF-8";
    
    public byte[] encoder(final String keypass, final byte[] input) {
        int inputLen = 0;
        byte[] KeyTemp = null;
        StringBuffer key = null;
        inputLen = input.length;
        key = new StringBuffer(keypass);
        while (key.length() < inputLen) {
            key.append(key);
        }
        try {
            KeyTemp = key.toString().getBytes("UTF-8");
            final byte[] encResult = new byte[inputLen];
            for (int i = 0; i < inputLen; ++i) {
                encResult[i] = (byte)((KeyTemp[i] & 0xFF) ^ (input[i] & 0xFF));
            }
            return encResult;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] decoder(final String keypass, final byte[] input) {
        int length = 0;
        try {
            final StringBuffer key = new StringBuffer(keypass);
            length = input.length;
            while (key.length() < length) {
                key.append(key);
            }
            final byte[] decResult = new byte[length];
            for (int i = 0; i < length; ++i) {
                decResult[i] = (byte)((byte)key.charAt(i) ^ input[i]);
            }
            return decResult;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
