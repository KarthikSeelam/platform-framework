package com.ican.cortex.platform.security.util;


import com.ican.cortex.platform.security.exception.AppException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


public class EncryptDecrypt {


    private static final String ALGORITHUM = "AES";
    private static final byte[] keyValue
            = new byte[]{'O', 'P', 'T', 'U', 'M', 'I', 'R',
            'M', '@', 'M', 'O', 'D', 'U', 'L', 'E', '$'};

    public String encrypt(String data) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHUM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(data.getBytes());
            String encryptedValue = new Base64(true).encodeToString(encVal);
            return (encryptedValue).replaceAll("([\\r\\n\\t])", "");
        } catch (Exception e) {
            throw new AppException("Exception Occurred while Encrypting:" + e.getMessage());
        }
    }

    public String decrypt(String encryptedData) throws AppException {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHUM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.decodeBase64(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new AppException("Exception Occurred while Decrypting:" + e.getMessage());
        }
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGORITHUM);
    }


}
