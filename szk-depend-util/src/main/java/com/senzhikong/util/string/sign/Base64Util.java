package com.senzhikong.util.string.sign;

import org.apache.commons.codec.binary.Base64;

/**
 * @author shu.zhou
 * @date 2018年5月23日 下午3:53:45
 */
public class Base64Util {

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        byte[] temp = Base64.decodeBase64(str.getBytes());
        return new String(temp);
    }

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static byte[] decode(byte[] str) {
        byte[] temp = Base64.decodeBase64(str);
        return temp;
    }

    public static byte[] decodeBuffer(String str) {
        byte[] temp = Base64.decodeBase64(str.getBytes());
        return temp;
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        byte[] temp = Base64.encodeBase64(str.getBytes());
        return new String(temp);
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static byte[] encode(byte[] str) {
        byte[] temp = Base64.encodeBase64(str);
        return temp;
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String encodeBuffer(byte[] str) {
        byte[] temp = Base64.encodeBase64(str);
        return new String(temp);
    }


}
