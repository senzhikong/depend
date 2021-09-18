package com.senzhikong.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static boolean isTelephone(String text) {
        Pattern pattern = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isEmail(String text) {
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isAccount(String text) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{5,15}$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isPassword(String text) {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,16}$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
