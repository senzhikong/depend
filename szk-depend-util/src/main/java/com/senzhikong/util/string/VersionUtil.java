package com.senzhikong.util.string;

/**
 * @author Shu.zhou
 * @date 2019年5月21日下午4:46:17
 */
public class VersionUtil {

    public static boolean compareVersion(String current, String lastest) {
        if (current.equals(lastest)) {
            return false;
        }
        String[] version1Array = current.split("[._]");
        String[] version2Array = lastest.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen &&
                (diff = Long.parseLong(version1Array[index]) - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return true;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return false;
                }
            }
            return false;
        } else {
            return diff > 0 ? true : false;
        }
    }
}
