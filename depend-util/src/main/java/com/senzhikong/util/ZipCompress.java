package com.senzhikong.util;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author shu
 */
public class ZipCompress {

    /**
     * @param files 文件列表（文件路径）
     * @param out   输出zip文件路径
     */
    public static void zipBuild(Map<String, String> files, OutputStream out) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (String fileName : files.keySet()) {
                String filePath = files.get(fileName);
                File file = new File(filePath);
                if (file.exists()) {
                    addZipFile(zos, file, fileName);
                }
            }
            zos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void addZipFile(ZipOutputStream zos, File file, String fileName) {
        int readLength;
        int bufferLength = 2048;
        byte[] buffer = new byte[bufferLength];
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setSize(file.length());
        zipEntry.setTime(file.lastModified());
        InputStream inputStream = null;
        try {
            zos.putNextEntry(zipEntry);
            inputStream = new BufferedInputStream(new FileInputStream(file));
            while ((readLength = inputStream.read(buffer, 0, bufferLength)) != -1) {
                zos.write(buffer, 0, readLength);
            }
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
