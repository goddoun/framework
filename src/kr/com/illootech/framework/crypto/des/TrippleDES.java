

package kr.com.illootech.framework.crypto.des;

import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import kr.com.illootech.framework.crypto.encoding.BASE64;
import javax.crypto.Cipher;
import kr.com.illootech.framework.file.log.Logger;

public class TrippleDES
{
    public static byte[] encoder(final byte[] input) {
        try {
            return encoder(new String(input)).getBytes();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static String encoder(final String input) throws Exception {
        try {
            if (input == null || input.length() == 0) {
                return null;
            }
            final String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
            final Cipher cipher = Cipher.getInstance(instance);
            cipher.init(1, getKey());
            final byte[] inputBytes1 = input.getBytes("UTF8");
            final byte[] encResult = cipher.doFinal(inputBytes1);
            final String result = BASE64.base64Encode(encResult);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static byte[] decoder(final byte[] input) {
        try {
            return encoder(new String(input)).getBytes();
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static String decoder(final String input) throws Exception {
        if (input == null || input.length() == 0) {
            return "";
        }
        final String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
        final Cipher cipher = Cipher.getInstance(instance);
        cipher.init(2, getKey());
        final byte[] b64Result = BASE64.base64Decode(new String(input));
        final byte[] decResult = cipher.doFinal(b64Result);
        final String strResult = new String(decResult, "UTF8");
        return strResult;
    }
    
    public static String key() {
        return "010203040506070801020304050607080102030405060708";
    }
    
    public static Key getKey() throws Exception {
        return (key().length() == 24) ? getKeyForTripleDES(key()) : getKeyForDES(key());
    }
    
    public static Key getKeyForDES(final String keyValue) throws Exception {
        final DESKeySpec desKeySpec = new DESKeySpec(keyValue.getBytes());
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        final Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
    
    public static Key getKeyForTripleDES(final String keyValue) throws Exception {
        final DESedeKeySpec desKeySpec = new DESedeKeySpec(keyValue.getBytes());
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
}
