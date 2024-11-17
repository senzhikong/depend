package com.senzhikong.web.context;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * @author shu.zhou
 */
public class ReactiveHttpContextHolder {
    public static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

    /**
     * 获取当前请求对象
     *
     * @return 请求对象
     */
    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.deferContextual(contextView -> Mono.just(contextView.get(CONTEXT_KEY)));
    }
}

