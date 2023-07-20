package com.senzhikong.web.util;

import com.senzhikong.util.string.StringUtil;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author shu
 */
@Slf4j
public class CrossUtil {
    public static void cors(HttpServletRequest request, HttpServletResponse response) {
        // 跨域问题
        String origin = request.getHeader("Origin");
        log.error("Origin::::::" + request.getHeader("Origin"));
        if (StringUtil.isEmpty(origin)) {
            origin = request.getHeader("origin");
            log.error("origin::::::" + request.getHeader("origin"));
        }
        if (StringUtil.isEmpty(origin)) {
            origin = "*";
        }
        //标识允许哪个域到请求，直接修改成请求头的域
        response.setHeader("Access-Control-Allow-Origin", origin);
        //标识允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        // 响应首部 Access-Control-Allow-Headers 用于 preflight request （预检请求）中，列出了将会在正式请求的 Access-Control-Expose-Headers 字段中出现的首部信息。修改为请求首部
        response.setHeader("Access-Control-Allow-Headers",
                String.format("Content-Type,content-type,Accept,Cookie,%s,%s", WebConstants.WEB_AUTH_TOKEN,
                        WebConstants.WEB_AUTH_TOKEN.toLowerCase()));
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
