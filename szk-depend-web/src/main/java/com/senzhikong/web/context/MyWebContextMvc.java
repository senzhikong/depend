package com.senzhikong.web.context;

import com.senzhikong.util.string.sign.Md5Util;
import com.senzhikong.web.util.WebConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

/**
 * 鉴权工具
 *
 * @author shu
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MyWebContextMvc implements MyWebContext {

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public Long getUserId() {
        Long userId = null;
        String str = getRequest().getHeader(WebConstants.X_HEADER_USER_ID);
        if (StringUtils.isNotBlank(str)) {
            userId = Long.parseLong(str);
        }
        return userId;
    }

    public String getToken() {
        return getRequest().getHeader(HttpHeaders.AUTHORIZATION);
    }
}
