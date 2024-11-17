package com.senzhikong.util.string.sign;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author shu
 */
public class DesUtil {

    private static DesUtil instance;
    /**
     * DES算法要求有一个可信任的随机数源
     */
    private SecureRandom random;
    /**
     * 将DESKeySpec对象转换成SecretKey对象
     */
    private final SecretKey securekey;
    /**
     * Cipher对象实际完成解密操作
     */
    private final Cipher cipher;

    private DesUtil() {
        try {
            // DES算法要求有一个可信任的随机数源
            random = new SecureRandom();
            // 创建一个DESKeySpec对象
            /**
             * 创建一个DESKeySpec对象
             */
            DESKeySpec desKey = new DESKeySpec(SignUtil.getPassword()
                    .getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            /**
             * 创建一个密匙工厂
             */
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            securekey = keyFactory.generateSecret(desKey);
            // 生成Cipher对象,指定其支持的DES算法
            cipher = Cipher.getInstance("DES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DesUtil getInstance() {
        if (instance == null) {
            instance = new DesUtil();
        }
        return instance;
    }

    public byte[] encode(byte[] src) {
        byte[] cipherByte;
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        try {
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            cipherByte = cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cipherByte;
    }

    public byte[] decode(byte[] buff) {
        byte[] cipherByte;
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        try {
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            cipherByte = cipher.doFinal(buff);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cipherByte;
    }

}
