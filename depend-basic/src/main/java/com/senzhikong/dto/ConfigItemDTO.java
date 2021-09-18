package com.senzhikong.dto;

import com.senzhikong.module.BaseConfigConstants;
import com.senzhikong.module.ConfigInterface;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("配置节点")
public class ConfigItemDTO {
    private String code;
    private String description;
    private String type;
    private boolean secret;
    private Object data;
    private Object value;

    public ConfigItemDTO(BaseConfigConstants constants, ConfigInterface config) {
        this.code = constants.code();
        this.description = constants.description();
        this.type = constants.getType();
        this.secret = constants.getSecret();
        this.data = constants.getData();
        if (secret) {
            return;
        }
        if ("longtext".equals(type)) {
            this.value = config.getConfigValue(this.code, "");
        } else if ("number".equals(type)) {
            this.value = new BigDecimal(config.getConfigValue(this.code));
        } else if ("boolean".equals(type)) {
            this.value = config.getBooleanConfig(this.code, false);
        } else if ("json".equals(type)) {
            this.value = config.getConfigValue(this.code, "{}");
        } else {
            this.value = config.getConfigValue(this.code, "");
        }

    }
}
