package com.senzhikong.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class AuthUtil {
    public static final String ADMIN_ID = "auth-admin-id";
    public static final String PARTNER_ID = "auth-partner-id";
    public static final String MEMBER_ID = "auth-member-id";

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static HttpSession getSession() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest()
                                                             .getSession();
    }

    public static Long getAdminId() {
        return Long.parseLong(getSession().getAttribute(ADMIN_ID)
                                          .toString());
    }

    public static Long getPartnerId() {
        return Long.parseLong(getSession().getAttribute(PARTNER_ID)
                                          .toString());
    }

    public static Long getMemberId() {
        return Long.parseLong(getSession().getAttribute(MEMBER_ID)
                                          .toString());
    }

    public static String getMemberToken() {
        return getRequest().getHeader(WebConstants.WEB_AUTH_TOKEN);
    }
}
