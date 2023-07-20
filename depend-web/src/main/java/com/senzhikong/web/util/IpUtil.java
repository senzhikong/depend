package com.senzhikong.web.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author Shu.zhou
 */
public class IpUtil {
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL1 = "0:0:0:0:0:0:0:1";
    private static final String LOCAL2 = "127.0.0.1";
    private static final String MULTIPLE_IP_SPLIT = ",";

    /**
     * @param request
     * @return
     * @desc：获取IP地址
     * @author Shu.zhou 2016年10月28日 下午5:49:26
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL1.equals(ip) || LOCAL2.equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(MULTIPLE_IP_SPLIT)) {
            ip = ip.substring(0, ip.indexOf(MULTIPLE_IP_SPLIT));
        }
        return ip;
    }
}
