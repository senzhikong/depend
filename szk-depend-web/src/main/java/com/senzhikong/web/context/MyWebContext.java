package com.senzhikong.web.context;

import com.senzhikong.auth.AuthConstant;
import com.senzhikong.util.string.sign.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.Locale;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

/**
 * 鉴权工具
 *
 * @author shu
 */
public interface MyWebContext {
    /**
     * 生成token
     *
     * @return token
     */
    default String generateToken() {
        return Md5Util.getInstance()
                .encode(UUID.randomUUID().toString() + System.currentTimeMillis(), "UTF-8",
                        "szk-login-token");
    }

    /**
     * 获取指定请求头信息
     *
     * @param header 请求头名称
     * @return 对应的请求头值
     */
    String getHeader(String header);

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    default String getUserId() {
        String str = getHeader(AuthConstant.X_HEADER_USER_ID);
        if (StringUtils.isNotBlank(str)) {
            return str;
        }
        return null;
    }


    /**
     * 获取当前请求token
     *
     * @return token
     */
    default String getToken() {
        return getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 获取当前请求的语言环境
     *
     * @return 根据请求头Accept-Language解析的Locale对象，如果未指定则返回系统默认Locale
     */
    default Locale getLocale() {
        String language = getHeader(ACCEPT_LANGUAGE);
        return (language != null && !language.isEmpty()) ? Locale.forLanguageTag(language) : Locale.getDefault();
    }
}
