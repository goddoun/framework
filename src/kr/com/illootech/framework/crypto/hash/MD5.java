

package kr.com.illootech.framework.crypto.hash;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class MD5
{
    public static byte[] MD5ByteEncoder(final byte[] input) {
        byte[] hashValue = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            hashValue = md.digest(input);
            return hashValue;
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
