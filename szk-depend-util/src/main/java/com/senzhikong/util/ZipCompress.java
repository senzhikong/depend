package com.senzhikong.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author shu
 */
@Slf4j
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
                log.error(e.getMessage(), e);
            }

        }
    }


    /**
     * @param files 文件列表（文件路径）
     * @param out   输出zip文件路径
     */
    public static void zipBuildFromStream(Map<String, InputStream> files, OutputStream out) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (String fileName : files.keySet()) {
                addZipFile(zos, files.get(fileName), fileName);
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
                log.error(e.getMessage(), e);
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
                log.error(e.getMessage(), e);
            }

        }
    }

    public static void addZipFile(ZipOutputStream zos, InputStream inputStream, String fileName) {
        int readLength;
        int bufferLength = 2048;
        byte[] buffer = new byte[bufferLength];
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setTime(System.currentTimeMillis());
        try {
            zipEntry.setSize(inputStream.available());
            zos.putNextEntry(zipEntry);
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
                log.error(e.getMessage(), e);
            }

        }
    }
}
