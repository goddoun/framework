

package kr.com.illootech.framework.crypto.aes;

import kr.com.illootech.framework.crypto.encoding.BASE64;
import java.security.Key;
import kr.com.illootech.framework.crypto.common.AnsiX923Padding;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

public class AES256
{
    private final int DEFAULT_AES_BITS = 256;
    private final String DEFAULT_CRYPTO_TYPE = "AES";
    private KeyGenerator key;
    private SecretKey sKey;
    private SecretKeySpec keySpec;
    private Cipher cipher;
    private String userKey;
    private AnsiX923Padding padding;
    
    public AES256() {
        this.key = null;
        this.sKey = null;
        this.keySpec = null;
        this.cipher = null;
        this.userKey = null;
        this.padding = null;
        this.padding = new AnsiX923Padding();
    }
    
    private boolean init(final String userKey) {
        boolean result = false;
        try {
            this.userKey = userKey;
            this.key = KeyGenerator.getInstance("AES");
            if (this.key == null) {
                return result;
            }
            this.key.init(256);
            this.sKey = this.key.generateKey();
            final byte[] raw = this.sKey.getEncoded();
            if (raw == null) {
                return result;
            }
            this.keySpec = new SecretKeySpec(this.userKey.getBytes(), "AES");
            this.cipher = Cipher.getInstance("AES");
            if (this.cipher == null) {
                return result;
            }
            result = true;
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String encrypt(final String userKey, final int blockSize, final String data, final String charset) {
        final String result = null;
        byte[] tmpByte = null;
        try {
            if (data == null || data.equals("")) {
                return result;
            }
            tmpByte = data.getBytes(charset);
            if (tmpByte == null) {
                return result;
            }
            tmpByte = this.encrypt(userKey, blockSize, tmpByte, charset);
            return new String(tmpByte, charset);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] encrypt(String userKey, final int blockSize, final byte[] data, final String charset) {
        byte[] result = null;
        byte[] encByte = null;
        byte[] paddingResult = null;
        try {
            if (userKey == null || userKey.equals("")) {
                return result;
            }
            if (userKey.length() < 16) {
                paddingResult = this.padding.addPadding(userKey.getBytes(charset), blockSize);
                if (paddingResult == null) {
                    return result;
                }
                userKey = new String(paddingResult, charset);
            }
            if (!this.init(userKey)) {
                return result;
            }
            this.cipher.init(1, this.keySpec);
            encByte = this.cipher.doFinal(data);
            result = BASE64.base64Encode(encByte).getBytes(charset);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String decrypt(final String userKey, final int blockSize, final String data, final String charset) {
        String result = null;
        byte[] encByte = null;
        byte[] decByte = null;
        try {
            if (data == null || data.equals("")) {
                return result;
            }
            encByte = data.getBytes(charset);
            if (encByte == null) {
                return result;
            }
            decByte = this.decrypt(userKey, blockSize, encByte, charset);
            result = new String(decByte, charset);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] decrypt(String userKey, final int blockSize, final byte[] data, final String charset) {
        byte[] result = null;
        byte[] decByte = null;
        byte[] paddingResult = null;
        try {
            if (userKey == null || userKey.equals("")) {
                return result;
            }
            if (userKey.length() < 16) {
                paddingResult = this.padding.addPadding(userKey.getBytes(charset), blockSize);
                if (paddingResult == null) {
                    return result;
                }
                userKey = new String(paddingResult, charset);
            }
            if (!this.init(userKey)) {
                return result;
            }
            this.cipher.init(2, this.keySpec);
            decByte = BASE64.base64Decode(new String(data, charset));
            result = this.cipher.doFinal(decByte);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final String message = "1234567890123456789012345678901234567890";
        final int DEFAULT_BLOCKSIZE = 16;
        final String userKey = "AAAABBBBCCCCDDDD";
        try {
            final AES256 aes = new AES256();
            final byte[] encrypted_1 = aes.encrypt(userKey, DEFAULT_BLOCKSIZE, message.getBytes(), "UTF-8");
            System.out.println("#1 encrypted string: " + new String(encrypted_1, "UTF-8") + ", " + encrypted_1.length + " bytes");
            final byte[] decrypted_1 = aes.decrypt(userKey, DEFAULT_BLOCKSIZE, encrypted_1, "UTF-8");
            System.out.println("#1 decrypted string : " + new String(decrypted_1, "UTF-8"));
            System.out.println("\n");
            final String encrypted_2 = aes.encrypt(userKey, DEFAULT_BLOCKSIZE, message, "UTF-8");
            System.out.println("#2 encrypted string: " + encrypted_2);
            final String decrypted_2 = aes.decrypt(userKey, DEFAULT_BLOCKSIZE, encrypted_2, "UTF-8");
            System.out.println("#2 decrypted string : " + decrypted_2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
