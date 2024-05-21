package com.senzhikong.cloud.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author milo
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DocumentInfoProperties.class)
public class DocumentConfiguration {
    @Bean
    public OpenAPI openApi(final DocumentInfoProperties docInfo) {

        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url(docInfo.getTryHost()));

        return new OpenAPI().info(new Info().title(docInfo.getTitle())
                                            .description(docInfo.getDescription()).version(docInfo.getVersion())
                                            .contact(new Contact().name(docInfo.getConcat()).email(docInfo.getEmail())
                                                                  .url(docInfo.getUrl()))
        ).servers(servers);
    }
}

