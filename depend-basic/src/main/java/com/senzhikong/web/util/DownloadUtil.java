package com.senzhikong.web.util;

import com.senzhikong.util.ZipCompress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Shu.zhou
 * @date 2019年3月12日下午6:34:24
 */
public class DownloadUtil {

    public static void write(String str, HttpServletResponse response) throws Exception {
        response.reset();
        response.setHeader("content-type", "text/html;charset=utf-8");
        response.getOutputStream()
                .write(str.getBytes());
    }

    public static void download(String path, String name, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (path == null) {
                String res = "<html><body><h2>文件未找到</h2></body></html>";
                write(res, response);
                return;
            }
            // path是指欲下载的文件的路径。
            File file = new File(path);
            if (!file.exists()) {
                String res = "<html><body><h2>文件未找到</h2></body></html>";
                write(res, response);
                return;
            }
            // 取得文件的后缀名。
            String ext = path.substring(path.lastIndexOf("."));
            // 取得文件名。
            String filename = name + ext;
            String agent = request.getHeader("User-Agent")
                    .toUpperCase(); // 获得浏览器信息并转换为大写
            if (agent.indexOf("MSIE") > 0 ||
                    (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) { // IE浏览器和Edge浏览器
                filename = URLEncoder.encode(filename, "UTF-8");
            } else { // 其他浏览器
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            // response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void download(Map<String, String> files, String name, HttpServletResponse response,
                                HttpServletRequest request) {
        try {
            if (files == null || files.isEmpty()) {
                String res = "<html><body><h2>文件未找到</h2></body></html>";
                write(res, response);
                return;
            }
            // 取得文件的后缀名。
            String ext = ".zip";
            // 取得文件名。
            String filename = name + ext;
            String agent = request.getHeader("User-Agent")
                    .toUpperCase(); // 获得浏览器信息并转换为大写
            if (agent.indexOf("MSIE") > 0 ||
                    (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) { // IE浏览器和Edge浏览器
                filename = URLEncoder.encode(filename, "UTF-8");
            } else { // 其他浏览器
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            // 清空response
            response.reset();
            // 设置response的Header
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            // response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            // response.setContentType("application/octet-stream");
            ZipCompress.zipBuild(files, toClient);
            toClient.flush();
            toClient.close();
            response.getOutputStream()
                    .close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static OutputStream getDownloadOutputStream(String filename, HttpServletResponse response,
                                                       HttpServletRequest request) throws Exception {
        String agent = request.getHeader("User-Agent")
                .toUpperCase(); // 获得浏览器信息并转换为大写
        if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) { // IE浏览器和Edge浏览器
            filename = URLEncoder.encode(filename, "UTF-8");
        } else { // 其他浏览器
            filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        // 清空response
        response.reset();
        // 设置response的Header
        response.setContentType("application/x-msdownload");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //            response.addHeader("Content-Length", "" + file.length());
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        return out;

    }
}
