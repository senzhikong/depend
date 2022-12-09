package com.senzhikong.web.util;

import com.senzhikong.util.string.sign.Md5Util;
import com.senzhikong.util.string.sign.SignUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 鉴权工具
 * @author shu
 */
public class AuthUtil {

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
        return Md5Util.getInstance()
                      .encode(getSession().getId() + System.currentTimeMillis(), "UTF-8", "szk-login-token");
    }

    public static String encryptPwd(String pwd) {
        return SignUtil.getShaUtil().encode(pwd, "UTF-8", "szk");
    }


    public static String generateEncryptCode() {
        return SignUtil.generateEncryptCode(8);
    }
}
