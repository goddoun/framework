

package kr.com.illootech.framework.crypto.des;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
import kr.com.illootech.framework.file.log.Logger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DES
{
    public static byte[] encoder(final byte[] data, final String passphrase) {
        byte[] result = null;
        final byte[] salt = { -87, -101, -56, 50, 86, 53, -29, 3 };
        final int iterationCount = 19;
        try {
            final PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 19);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            final Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            final PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 19);
            ecipher.init(1, key, paramSpec);
            result = ecipher.doFinal(data);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    
    public static byte[] decoder(final byte[] data, final String passphrase) {
        byte[] result = null;
        final byte[] salt = { -87, -101, -56, 50, 86, 53, -29, 3 };
        final int iterationCount = 19;
        try {
            final PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, 19);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            final Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            final PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 19);
            dcipher.init(2, key, paramSpec);
            result = dcipher.doFinal(data);
            return result;
        }
        catch (BadPaddingException e) {
            Logger.error(e);
            return null;
        }
        catch (Exception e2) {
            Logger.error(e2);
            return null;
        }
    }
    
    public static void main(final String[] args) {
        final String input = "FFBD7C7BFB448A1F";
        System.out.println("DEC : " + new String(decoder(input.getBytes(), "1A3280628F07A4D3")));
    }
}
