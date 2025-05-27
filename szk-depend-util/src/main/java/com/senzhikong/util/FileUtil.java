package com.senzhikong.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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


    /**
     * 将输入流转换为字符串
     *
     * @param inputStream 需要转换的输入流
     * @return 转换后的字符串
     * @throws RuntimeException 如果读取输入流时发生IO异常
     * @see InputStream 输入流
     * @see BufferedReader 缓冲读取器
     * @see StringBuilder 字符串构建器
     */
    public static String streamToString(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 将MultipartFile对象转换为字符串
     *
     * @param file 需要转换的MultipartFile对象
     * @return 文件内容转换后的字符串
     * @throws RuntimeException 如果读取文件时发生IO异常
     * @see MultipartFile Spring框架中的文件上传对象
     * @see InputStream 输入流
     */
    public static String multipartFileToString(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return streamToString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
