package com.senzhikong.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Shu.zhou
 * @date 2019年1月18日下午4:53:16
 */
public class FileUtil {

    public static void saveFile(InputStream is, String filePath) {

        OutputStream os = null;
        try {
            byte[] bs = new byte[1024];
            int len;
            File tempFile = new File(filePath);
            boolean flg = tempFile.getParentFile().mkdirs();
            if (!flg) {
                throw new RuntimeException("文件夹【" + tempFile.getParentFile() + "】创建失败");
            }
            os = new FileOutputStream(tempFile);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
