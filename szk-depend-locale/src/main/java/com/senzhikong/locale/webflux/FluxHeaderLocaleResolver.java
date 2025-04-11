package com.senzhikong.locale.webflux;

import lombok.NonNull;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

/**
 * @author shu.zhou
 */
public class FluxHeaderLocaleResolver implements LocaleContextResolver {
    @Override
    @NonNull
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        // 从请求中解析区域设置
        String language = exchange.getRequest().getHeaders().getFirst(ACCEPT_LANGUAGE);
        Locale locale = (language != null && !language.isEmpty()) ? Locale.forLanguageTag(language) : Locale.getDefault();
        return () -> locale;
    }

    @Override
    public void setLocaleContext(@NonNull ServerWebExchange exchange, LocaleContext localeContext) {
        // 设置区域设置的逻辑（如果需要）
    }
}
