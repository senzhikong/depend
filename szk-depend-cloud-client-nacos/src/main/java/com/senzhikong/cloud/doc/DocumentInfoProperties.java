package com.senzhikong.cloud.doc;

import lombok.Data;
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
    private String title = "数字人社";
    /**
     * 文档描述
     */
    private String description = "数字人社";
    /**
     * 项目version
     */
    private String version = "1.0.1";
    /**
     * 联系邮箱
     */
    private String email = "";
    /**
     * 联系人
     */
    private String concat = "";
    /**
     * 网站
     */
    private String url = "www.goldpac.com";
    /**
     * 测试地址
     */
    private String tryHost = "http://12.0.0.1:9200";
}

