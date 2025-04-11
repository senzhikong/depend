package com.senzhikong.locale.cache;

import com.senzhikong.locale.AbstractMyMessageSource;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author shu.zhou
 */
@Component("messageSource")
@ConditionalOnProperty(prefix = "szk.locale", name = "type", havingValue = "cache")
public class CacheLocaleMessageSource extends AbstractMyMessageSource {
    @Resource
    private LocaleCache localeCache;

    @Override
    protected MessageFormat resolveCode(@NonNull String code, @NonNull Locale locale) {
        return createMessageFormat(localeCache.findLocaleString(code, locale.toString()), locale);
    }
}
