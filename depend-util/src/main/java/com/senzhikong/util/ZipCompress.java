package com.senzhikong.util;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author shu
 */
public class ZipCompress {


    public static void zipBuild(Map<String, String> files, OutputStream out) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(out);
        Iterator<String> it = files.keySet()
                .iterator();
        while (it.hasNext()) {
            String fileName = it.next();
            String filePath = files.get(fileName);
            File file = new File(filePath);
            if (file.exists()) {
                addZipFile(zos, file, fileName);
            }
        }
        zos.close();
    }

    public static void addZipFile(ZipOutputStream zos, File file, String fileName) throws Exception {
        int readLength = 0;
        int bufferLength = 2048;
        byte[] buffer = new byte[bufferLength];
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setSize(file.length());
        zipEntry.setTime(file.lastModified());
        zos.putNextEntry(zipEntry);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        while ((readLength = inputStream.read(buffer, 0, bufferLength)) != -1) {
            zos.write(buffer, 0, readLength);
        }
        inputStream.close();
    }
}
