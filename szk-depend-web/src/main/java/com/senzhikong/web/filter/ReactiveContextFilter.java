package com.senzhikong.web.filter;

import com.senzhikong.web.context.ReactiveHttpContextHolder;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author shu.zhou
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveContextFilter implements WebFilter {
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return Mono.deferContextual(contextView -> {
            ServerHttpRequest request = exchange.getRequest();
            return chain.filter(exchange)
                    .contextWrite(context -> context.put(ReactiveHttpContextHolder.CONTEXT_KEY, request));
        });
    }
}
