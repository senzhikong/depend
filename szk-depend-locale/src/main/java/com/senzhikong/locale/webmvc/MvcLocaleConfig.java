package com.senzhikong.locale.webmvc;

import com.senzhikong.locale.AbstractMyMessageSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shu.zhou
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "szk.locale", name = "enable", havingValue = "true")
public class MvcLocaleConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        return new MvcHeaderLocaleResolver();
    }

    @Bean
    public LocalValidatorFactoryBean validator(AbstractMyMessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }
}
