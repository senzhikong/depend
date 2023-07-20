package com.senzhikong.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.config.AbstractConfigInterface;
import com.senzhikong.util.string.StringUtil;
import com.senzhikong.web.util.WebConstants;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author Shu.zhou
 */
@Slf4j
public class BaseController {

    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
    @Resource
    protected HttpSession session;
    @Resource
    protected AbstractConfigInterface config;

    public void setCookie(String key, Object value, Integer time) {
        if (time == null) {
            time = 30 * 24 * 60 * 60;
        }
        Cookie userCookie = new Cookie(key, String.valueOf(value));
        // 存活期为一个月 30*24*60*60
        userCookie.setMaxAge(time);
        response.addCookie(userCookie);
    }

    public String getCookie(String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtil.equal(key, cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Integer HTTP_PORT = 80;
    public static Integer HTTPS_PORT = 80;

    public String getBaseUrl() {
        String path = request.getContextPath();
        String scheme = request.getScheme();
        String port = ":" + request.getServerPort();
        if (request.getServerPort() == HTTP_PORT || request.getServerPort() == HTTPS_PORT) {
            port = "";
        }
        return scheme + "://" + request.getServerName() + port + path;
    }

    public JSONObject getRequestParams() {
        JSONObject param = new JSONObject();
        if (request.getParameterNames() != null) {
            Enumeration<String> keys = request.getParameterNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String[] values = request.getParameterValues(key);
                JSONArray value = new JSONArray();
                value.addAll(Arrays.asList(values));
                param.put(key, value);
            }
        }
        return param;
    }

    protected String getAuthorization() {
        return request.getHeader(WebConstants.WEB_AUTH_TOKEN);
    }
}
