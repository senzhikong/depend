package com.senzhikong.cloud.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author milo
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DocumentInfoProperties.class)
public class DocumentConfiguration {
    @Bean
    public OpenAPI openApi(final DocumentInfoProperties docInfo) {
        Info info = new Info().title(docInfo.getTitle()).version(docInfo.getVersion());
        info.contact(new Contact().name(docInfo.getConcat()).email(docInfo.getEmail()).url(docInfo.getUrl()));
        return new OpenAPI().info(info);
    }
}

