package com.senzhikong.util.string.sign;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AESUtil {

    public static final String SIGN_ALGORITHMS = "SHA1PRNG";
    private static AESUtil instance;

    private AESUtil() {
    }

    public static AESUtil getInstance() {
        if (instance == null) {
            instance = new AESUtil();
        }
        return instance;
    }

    public static SecretKey getKey(String encrypt) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance(SIGN_ALGORITHMS);
            secureRandom.setSeed(encrypt.getBytes());
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

    /**
     * 加密
     *
     */
    public String encode(String content, String encrypt) {
        try {
            SecretKey originalKey = getKey(encrypt);
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
            byte[] byteAES = cipher.doFinal(byteEncode);
            return new String(Base64Util.encode(byteAES));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     */
    public String decode(String content, String encrypt) {
        try {
            SecretKey original_key = getKey(encrypt);
            byte[] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byte_content = Base64Util.decodeBuffer(content);
            byte[] byte_decode = cipher.doFinal(byte_content);
            return new String(byte_decode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
