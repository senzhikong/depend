package com.senzhikong.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shu
 */
public class RegexUtil {
    public static boolean isTelephone(String text) {
        String regexp="^(13\\d|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18\\d|19[0-35-9])\\d{8}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isEmail(String text) {
        String regexp="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isAccount(String text) {
        String regexp="^[a-zA-Z]\\w{5,15}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isPassword(String text) {
        String regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])\\w{8,16}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
