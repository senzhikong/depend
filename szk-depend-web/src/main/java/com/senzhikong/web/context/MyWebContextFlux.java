package com.senzhikong.web.context;

import com.senzhikong.web.util.WebConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * 鉴权工具
 *
 * @author shu
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class MyWebContextFlux implements MyWebContext {

    public ServerHttpRequest getRequest() {
        try {
            return ReactiveHttpContextHolder.getRequest().toFuture().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long getUserId() {
        Long userId = null;
        String str = getRequest().getHeaders().getFirst(WebConstants.X_HEADER_USER_ID);
        if (StringUtils.isNotBlank(str)) {
            userId = Long.parseLong(str);
        }
        return userId;
    }

    public String getToken() {
        return getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }
}
