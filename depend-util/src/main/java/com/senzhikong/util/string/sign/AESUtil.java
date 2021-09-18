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
        if (instance == null)
            instance = new AESUtil();
        return instance;
    }

    public static SecretKey getKey(String encrypt) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = null;
            String os = System.getProperty("os.name");
            if (os.toLowerCase()
                    .startsWith("win")) {
                //                secureRandom = new SecureRandom(encrypt.getBytes());
                secureRandom = SecureRandom.getInstance(SIGN_ALGORITHMS);
                secureRandom.setSeed(encrypt.getBytes());
            } else {
                secureRandom = SecureRandom.getInstance(SIGN_ALGORITHMS);
                secureRandom.setSeed(encrypt.getBytes());
            }
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

    /**
     * 加密
     *
     * @param content
     * @param encrypt
     * @return
     */
    public String encode(String content, String encrypt) {
        try {
            SecretKey original_key = getKey(encrypt);
            byte[] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byte_encode = content.getBytes(StandardCharsets.UTF_8);
            byte[] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(Base64Util.encode(byte_AES));
            return AES_encode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param content
     * @param encrypt
     * @return
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
            String AES_decode = new String(byte_decode, StandardCharsets.UTF_8);
            return AES_decode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}