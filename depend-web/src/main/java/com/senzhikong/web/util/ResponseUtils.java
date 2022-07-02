package com.senzhikong.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.senzhikong.web.ajax.ApiStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 返回数据封装
 *
 * @author Shu.zhou
 */
public class ResponseUtils {

    /**
     * 设置json 全局时间转换格式
     */
    private static SerializerFeature[] sf =
            {SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat};

    /**
     * 返回文本
     *
     * @param response    HttpServletResponse
     * @param contentType 文本格式
     * @param text        返回的字符串
     */
    public static void render(HttpServletResponse response, String contentType, Object text) {
        if (null == text) {
            text =  ApiStatus.ERROR.message();
        }
        response.setContentType(contentType);
        try {
            response.getWriter()
                    .write(text.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renderFile(HttpServletResponse response, File file) {
        try (ServletOutputStream os = response.getOutputStream(); FileInputStream fileInputStream = new FileInputStream(
                file)) {
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(file.getName()
                    .getBytes(
                            StandardCharsets.UTF_8),
                    "ISO8859-1"));
            int temp;
            while ((temp = fileInputStream.read()) != -1) {
                os.write(temp);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回Json
     *
     * @param response HttpServletResponse
     * @param text     返回的字符串
     */
    public static void renderJson(HttpServletResponse response, Object text) {
        render(response, "application/json;charset=UTF-8", JSON.toJSONString(text, sf));
    }

    public static void renderJsonP(HttpServletResponse response, Object callback, Object text) throws Exception {
        if (ObjectUtils.isEmpty(callback)) {
            render(response, "application/json;charset=UTF-8", text);
            return;
        }
        response.setContentType("application/x-javascript");
        PrintWriter out = response.getWriter();
        // 返回jsonp格式数据
        out.println(callback + "(" + JSON.toJSONString(text) + ")");
        out.flush();
        out.close();
    }

    /**
     * 返回Xml
     *
     * @param response HttpServletResponse
     * @param text     返回的字符串
     */
    public static void renderXml(HttpServletResponse response, Object text) {
        render(response, "text/xml;charset=UTF-8", text);
    }

    /**
     * 返回Text
     *
     * @param response HttpServletResponse
     * @param text     返回的字符串
     */
    public static void renderText(HttpServletResponse response, Object text) {
        render(response, "text/plain;charset=UTF-8", text);
    }
}
