package com.senzhikong.web.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shu.Zhou
 */
public class UserAgentUtil {

    /**
     * 判断 移动端/PC端
     *
     * @param request
     * @return
     * @Title: isMobile
     * @author: pk
     * @return: boolean
     */
    public static boolean isMobile(HttpServletRequest request) {
        List<String> mobileAgents =
                Arrays.asList("ipad", "iphone os", "rv:1.2.3.4", "ucweb", "android", "windows ce", "windows mobile");
        String ua = request.getHeader("User-Agent")
                .toLowerCase();
        for (String sua : mobileAgents) {
            // 手机端
            if (ua.contains(sua)) {
                return true;
            }
        }
        // PC端
        return false;
    }

    /**
     * 是否微信浏览器
     *
     * @param request
     * @return
     * @Title: isWechat
     * @author: pk
     * @return: boolean
     */
    public static boolean isWechat(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent")
                .toLowerCase();
        return ua.contains("micromessenger");
    }

    public static boolean isAlipay(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent")
                .toLowerCase();
        return ua.contains("alipayclient");
    }
}
