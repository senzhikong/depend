package com.senzhikong.web.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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

    @Override
    public String getHeader(String header) {
        return getRequest().getHeaders().getFirst(header);
    }
}
