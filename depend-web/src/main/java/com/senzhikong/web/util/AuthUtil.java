package com.senzhikong.web.util;

import com.senzhikong.util.string.sign.MD5Util;
import com.senzhikong.util.string.sign.SignUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class AuthUtil {
    public static final String ADMIN_MODEL = "auth-admin-model";
    public static final String PARTNER_MODEL = "auth-partner-model";
    public static final String MEMBER_MODEL = "auth-member-model";

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static HttpSession getSession() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest()
                                                             .getSession();
    }

    public static Long getUserId() {
        Object res = getSession().getAttribute(WebConstants.LOGIN_USER_ID);
        return res == null ? null : Long.parseLong(res.toString());
    }

    public static String getAuthToken() {
        return getRequest().getHeader(WebConstants.WEB_AUTH_TOKEN);
    }

    public static String generateToken() {
        return MD5Util.getInstance()
                      .encode(getSession().getId() + System.currentTimeMillis(), "UTF-8", "admin-login-token");
    }

    public static String encryptPwd(String pwd) {
        return SignUtil.getSHAUtil().encode(pwd, "UTF-8", "senzhikong");
    }


    public static String generateEncryptCode() {
        return SignUtil.generateEncryptCode(8);
    }
}
