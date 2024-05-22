package com.senzhikong.cloud.doc;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * The type Swagger properties.
 *
 * @author : Milo
 */
@Data
@Component
@Configuration
public class DocumentInfoProperties {

    /**
     * 文档标题
     */
    @Value("${info.app.name}")
    private String title;
    /**
     * 文档描述
     */
    @Value("${info.app.desc}")
    private String description;
    /**
     * 项目version
     */
    @Value("${info.build.version}")
    private String version;
    /**
     * 联系邮箱
     */
    @Value("${info.company.email}")
    private String email;
    /**
     * 联系人
     */
    @Value("${info.company.concat}")
    private String concat;
    /**
     * 网站
     */
    @Value("${info.company.url}")
    private String url = "www.zhou-shu.com";
}

