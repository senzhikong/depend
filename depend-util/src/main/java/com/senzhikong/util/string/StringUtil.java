package com.senzhikong.util.string;

import java.util.List;

/**
 * @author Shu.zhou
 * @date 2018年11月30日上午9:24:58
 */
public class StringUtil {
    public static final String EMPTY = "";

    /**
     * 判断是否为空
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 判断是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return (!isEmpty(str));
    }

    public static boolean equal(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
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
            sb.append(Character.toLowerCase(c));  //统一都转小写
        }
        return sb.toString();
    }

    public static String join(List<Object> list, String separator) {
        return join(list.toArray(), separator);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            buf.append(array[i]);
        }
        return buf.toString();
    }
}
