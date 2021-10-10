package com.senzhikong.util.string.sign;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {
    private static final String src = "abcdefghijklmnopqrstuvwxyz";


    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final int MAX_DECRYPT_BLOCK = 128;

    private static byte[] encryptOrDecrypt(byte[] data, Cipher cipher, int maxBlock) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText 公钥
     * @param text          需要解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(encryptOrDecrypt(Base64.decodeBase64(text), cipher, MAX_DECRYPT_BLOCK));
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText 私钥
     * @param text           需要加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.encodeBase64String(encryptOrDecrypt(text.getBytes(), cipher, MAX_ENCRYPT_BLOCK));
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText 私钥
     * @param text           需要解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(encryptOrDecrypt(Base64.decodeBase64(text), cipher, MAX_DECRYPT_BLOCK));
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText 公钥
     * @param text          需要加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(encryptOrDecrypt(text.getBytes(), cipher, MAX_ENCRYPT_BLOCK));
    }

    /**
     * 构建RSA密钥对
     *
     * @return 密钥对
     */
    public static RSAKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        RSAKeyPair rsaKeyPair = new RSAKeyPair();
        rsaKeyPair.setPrivateKey(privateKeyString);
        rsaKeyPair.setPublicKey(publicKeyString);
        return rsaKeyPair;
    }


    /**
     * RSA密钥对对象
     */
    @Data
    public static class RSAKeyPair {
        private String publicKey;
        private String privateKey;
    }
}
