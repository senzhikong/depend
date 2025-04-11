package com.senzhikong.locale.webflux;

import com.senzhikong.locale.AbstractMyMessageSource;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shu.zhou
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(prefix = "szk.locale", name = "enable", havingValue = "true")
public class FluxLocaleConfig implements WebMvcConfigurer {
    @Bean
    public LocaleContextResolver localeResolver() {
        return new FluxHeaderLocaleResolver();
    }

    @Bean
    public LocalValidatorFactoryBean validator(AbstractMyMessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }
}
