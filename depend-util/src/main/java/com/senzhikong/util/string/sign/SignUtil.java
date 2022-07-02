package com.senzhikong.util.string.sign;

import com.senzhikong.util.string.StringUtil;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 加密工具类
 *
 * @author zs614
 */
public class SignUtil {

    public static final String CODE = "0123456789abcdefgknpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    private static final String PASSWORD =
            "95880288210109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成加密字符
     *
     * @param verifySize 验证码长度
     * @return
     */
    public static String generateEncryptCode(int verifySize) {
        return generateEncryptCode(verifySize, CODE);
    }

    /**
     * 生成加密字符
     *
     * @param verifySize 验证码长度
     * @param sources    验证码字符源
     * @return
     */
    public static String generateEncryptCode(int verifySize, String sources) {
        if (StringUtil.isEmpty(sources)) {
            sources = CODE;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    public static Md5Util getMd5Util() {
        return Md5Util.getInstance();
    }

    public static ShaUtil getShaUtil() {
        return ShaUtil.getInstance();
    }

    public static DesUtil getDesUtil() {
        return DesUtil.getInstance();
    }

    public static AesUtil getAesUtil() {
        return AesUtil.getInstance();
    }


    public static String getPassword() {
        return PASSWORD;
    }

    public static String generateNonceStr(int length) {
        char[] nonceChars = new char[length];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static String byteToStr(byte[] byteArray) {
        StringBuilder rst = new StringBuilder();
        for (byte b : byteArray) {
            rst.append(byteToHex(b));
        }
        return rst.toString();
    }

    public static String byteToHex(byte b) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = digit[(b >>> 4) & 0X0F];
        tempArr[1] = digit[b & 0X0F];
        return new String(tempArr);
    }
}
