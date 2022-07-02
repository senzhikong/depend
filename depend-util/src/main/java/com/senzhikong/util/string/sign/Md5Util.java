package com.senzhikong.util.string.sign;

import java.security.MessageDigest;

/**
 * @author shu
 */
public class Md5Util {
    private static Md5Util instance;

    private Md5Util() {
    }

    public static Md5Util getInstance() {
        if (instance == null) {
            instance = new Md5Util();
        }
        return instance;
    }

    public String encode(String info, String encoding, String key) {
        String md5Password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] infoBytes;
            if (key != null && !"".equals(key)) {
                info += key;
            }
            if (encoding != null && !"".equals(encoding.trim())) {
                infoBytes = info.getBytes(encoding);
            } else {
                infoBytes = info.getBytes();
            }
            md.update(infoBytes);
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            md5Password = buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return md5Password;
    }

    public String encode(String info) {
        return encode(info, "UTF-8", "");
    }

    public String encode(String info, String key) {
        return encode(info, "UTF-8", key);
    }
}
