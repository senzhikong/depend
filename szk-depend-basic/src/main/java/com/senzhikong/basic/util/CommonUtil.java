package com.senzhikong.basic.util;

import com.senzhikong.basic.exception.DataException;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * 共享工具看
 *
 * @author shu.zhou
 */
public class CommonUtil {

    public static void checkStringNull(String obj, String err) {
        if (StringUtils.isBlank(obj)) {
            throw new DataException(err);
        }
    }

    public static void checkNull(Object obj, String err) {
        if (obj == null) {
            throw new DataException(err);
        }
    }

    public static void throwError(String err) {
        throw new DataException(err);
    }

    public static void checkNull(Object obj, RuntimeException err) {
        if (obj == null) {
            throw err;
        }
    }


    public static final String VERIFY_CODES = "23456789abcdefgknpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * 使用系统默认字符源生成验证码
     *
     * @param verifySize 验证码长度
     */
    public static String generateRandomStr(int verifySize) {
        return generateRandomStr(verifySize, VERIFY_CODES);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param verifySize 验证码长度
     * @param sources    验证码字符源
     */
    public static String generateRandomStr(int verifySize, String sources) {
        if (sources == null || sources.isEmpty()) {
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(RANDOM.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
            }
            //统一都转小写
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    private static final char UN_ICON = '_';

    public static String underlineToCamel(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(param.charAt(i));
            if (c == UN_ICON) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
