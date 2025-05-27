package com.senzhikong.locale;

import com.senzhikong.web.context.MyWebContext;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author shu.zhou
 */
@Component
@ConditionalOnProperty(prefix = "szk.locale", name = "enable", havingValue = "true")
public class LocaleTool implements InitializingBean {
    @Resource
    private AbstractMyMessageSource myMessageSource;
    private static LocaleTool INSTANCE;
    @Resource
    private MyWebContext webContext;

    public static String getMessage(String code) {
        Locale locale = INSTANCE.webContext.getLocale();
        return INSTANCE.myMessageSource.getMessage(code, null, locale);
    }

    @Override
    public void afterPropertiesSet() {
        INSTANCE = this;
    }
}
