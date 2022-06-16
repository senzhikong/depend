package com.senzhikong.util.http;

import com.senzhikong.util.ZipCompress;
import com.senzhikong.util.string.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 文件下载工具类
 *
 * @author zs614
 */
public class DownloadUtil {

    /**
     * http协议下载文件
     */
    public static DownloadFile downloadHttp(String url, String file) throws Exception {
        return downloadHttp(url, new File(file), null);
    }

    /**
     * http协议下载文件
     */
    public static DownloadFile downloadHttp(String url, String file, DownloadFile info) throws Exception {
        try {
            return downloadHttp(url, new File(file), info);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * http协议下载文件
     */
    public static DownloadFile downloadHttp(String url, File file) throws Exception {
        return downloadHttp(url, file, null);
    }

    public static void createParentDirs(File file) {
        if (!file.exists()) {
            File pFile = new File(file.getAbsolutePath());
            pFile = pFile.getParentFile();
            boolean flag = pFile.mkdirs();
            if (!flag) {
                throw new RuntimeException(pFile.getAbsolutePath() + "文件夹创建失败");
            }
        }
    }

    /**
     * http协议下载文件
     */
    public static DownloadFile downloadHttp(String url, File file, DownloadFile info) throws Exception {
        FileOutputStream fos;
        try {
            createParentDirs(file);
            fos = new FileOutputStream(file);
            return downloadHttp(url, fos, info);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * http协议下载文件
     */
    public static DownloadFile downloadHttp(String url, OutputStream out) throws Exception {
        return downloadHttp(url, out, null);
    }

    public static String getRedirectUrl(HttpURLConnection connection) {
        Map<String, List<String>> map = connection.getHeaderFields();
        for (String key : map.keySet()) {
            // 如果发现有重定向了新的地址
            if ("Location".equals(key)) {
                // 获取新地址
                return map.get(key)
                          .get(0);
            }
        }
        return null;
    }


    /**
     * http协议下载文件
     */
    public static HttpURLConnection getDownloadConnection(String url) throws Exception {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        while (StringUtil.isNotEmpty(url = getRedirectUrl(connection))) {
            httpUrl = new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();
        }
        return connection;
    }

    /**
     * http协议下载文件
     */
    public static DownloadFile downloadHttp(String url, OutputStream out, DownloadFile info) throws Exception {
        // 此方法只能用户HTTP协议
        DataInputStream in = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            if (url.startsWith(HttpUtil.HTTPS)) {
                HttpUtil.useHttps(connection);
            }
            while (StringUtil.isNotEmpty(url = getRedirectUrl(connection))) {

                httpUrl = new URL(url);
                connection = (HttpURLConnection) httpUrl.openConnection();
            }
            if (info == null) {
                info = new DownloadFile();
            }
            info.setTotalSize(connection.getContentLengthLong());
            in = new DataInputStream(connection.getInputStream());
            DataOutputStream dos = new DataOutputStream(out);
            byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) > 0) {
                dos.write(buffer, 0, count);
                info.setNow(info.getNow() + count);
            }
            String text = connection.getContentType();
            info.setContentType(text);
            dos.close();
            in.close();
            return info;
        } catch (Exception e) {
            throw new Exception("文件下载失败：" + url, e);
        } finally {
            try {
                out.close();
                assert in != null;
                in.close();
            } catch (Exception ignored) {
            }
            assert info != null;
            info.setFinish(true);
        }
    }

    /**
     * http或ftp协议下载文件
     */
    public static boolean downloadHttpAndFtp(String url, String file) throws Exception {
        return downloadHttpAndFtp(url, new File(file), null);
    }

    /**
     * http或ftp协议下载文件
     */
    public static boolean downloadHttpAndFtp(String url, String file, DownloadFile rate) throws Exception {
        try {
            return downloadHttpAndFtp(url, new File(file), rate);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * http或ftp协议下载文件
     */
    public static boolean downloadHttpAndFtp(String url, File file) throws Exception {
        return downloadHttpAndFtp(url, file, null);
    }

    /**
     * http或ftp协议下载文件
     */
    public static boolean downloadHttpAndFtp(String url, File file, DownloadFile rate) throws Exception {
        FileOutputStream fos;
        try {
            createParentDirs(file);
            fos = new FileOutputStream(file);
            return downloadHttpAndFtp(url, fos, rate);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * http或ftp协议下载文件
     */
    public static boolean downloadHttpAndFtp(String url, OutputStream out) throws Exception {
        return downloadHttpAndFtp(url, out, null);
    }

    /**
     * http或ftp协议下载文件
     */
    public static boolean downloadHttpAndFtp(String urlString, OutputStream out, DownloadFile rate) throws Exception {
        // 此方法兼容HTTP和FTP协议
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            in = conn.getInputStream();
            if (rate != null) {
                rate.setTotalSize(conn.getContentLengthLong());
            }
            byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
                if (rate != null) {
                    rate.setNow(rate.getNow() + count);
                }
            }
            return true;
        } catch (Exception e) {
            throw new Exception("文件下载失败：" + urlString, e);
        } finally {
            try {
                out.close();
                assert in != null;
                in.close();
            } catch (Exception ignored) {
            }
        }
    }


}
