package com.senzhikong.util.string.sign;

import java.security.MessageDigest;

public class SHAUtil {
    private static SHAUtil instance;

    private SHAUtil() {
    }

    public static SHAUtil getInstance() {
        if (instance == null)
            instance = new SHAUtil();
        return instance;
    }

    public String encode(String info, String encoding, String key) {
        String password;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] infoBytes = null;
            if (key != null && !"".equals(key))
                info += key;
            if (encoding != null && !"".equals(encoding.trim())) {
                infoBytes = info.getBytes(encoding);
            } else {
                infoBytes = info.getBytes();
            }
            md.update(infoBytes);
            byte[] b = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            password = buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return password;
    }
}
