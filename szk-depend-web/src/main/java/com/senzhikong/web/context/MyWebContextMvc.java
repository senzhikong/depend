package com.senzhikong.web.context;

import com.senzhikong.auth.AuthConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

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

    @Override
    public String getUserId() {
        String str = getRequest().getHeader(AuthConstant.X_HEADER_USER_ID);
        if (StringUtils.isNotBlank(str)) {
            return str;
        }
        return null;
    }

    @Override
    public String getToken() {
        return getRequest().getHeader(HttpHeaders.AUTHORIZATION);
    }
}
