

package kr.com.illootech.framework.crypto.hash;

import kr.com.illootech.framework.utils.operator.ByteOperator;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class SHA
{
    private final String DEFAULT_CHARSET = "UTF-8";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";
    
    public byte[] SHAWithByte(final String algorithm, final byte[] input) {
        byte[] retVal = null;
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(input);
            retVal = md.digest();
            return retVal;
        }
        catch (NoSuchAlgorithmException nae) {
            nae.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] SHAWithString(final String algorithm, final String input) {
        byte[] retVal = null;
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(input.getBytes("UTF-8"));
            retVal = md.digest();
            return retVal;
        }
        catch (NoSuchAlgorithmException nae) {
            nae.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(final String[] args) {
        try {
            final SHA sha = new SHA();
            final String input = "V2ViU29ja2V0IHJvY2tzIQ==258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            byte[] encByte = sha.SHAWithString("SHA-1", input);
            System.out.println("result >> " + ByteOperator.ByteToHexStr(encByte, 0, encByte.length) + "::" + "540b8681a34307fade550a467c317c297799c79a");
            encByte = sha.SHAWithString("SHA-512", input);
            System.out.println("result >> " + ByteOperator.ByteToHexStr(encByte, 0, encByte.length));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
