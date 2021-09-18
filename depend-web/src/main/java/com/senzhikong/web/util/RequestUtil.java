package com.senzhikong.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求处理-工具
 *
 * @author wd
 */
public class RequestUtil {

    /**
     * 判断是否ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

}
