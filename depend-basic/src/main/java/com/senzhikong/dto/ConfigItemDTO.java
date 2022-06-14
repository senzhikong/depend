package com.senzhikong.dto;

import com.senzhikong.config.BaseConfigConstants;
import com.senzhikong.config.ConfigInterface;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author shu
 */
@Data
@NoArgsConstructor
@ApiModel("配置节点")
public class ConfigItemDTO implements Serializable {
    private String code;
    private String description;
    private String type;
    private Boolean secret;
    private Object data;
    private Object value;

    public ConfigItemDTO(BaseConfigConstants constants, ConfigInterface config) {
        this.code = constants.getCode();
        this.description = constants.getDescription();
        this.type = constants.getType();
        this.secret = constants.isSecret();
        this.data = constants.getData();
        if (secret) {
            return;
        }
        if ("longtext".equals(type)) {
            this.value = config.getConfigValue(this.code, "");
        } else if ("number".equals(type)) {
            if (StringUtils.isNotBlank(config.getConfigValue(this.code))) {
                this.value = new BigDecimal(config.getConfigValue(this.code));
            }
        } else if ("boolean".equals(type)) {
            this.value = config.getBooleanConfig(this.code, false);
        } else if ("json".equals(type)) {
            this.value = config.getConfigValue(this.code, "{}");
        } else {
            this.value = config.getConfigValue(this.code, "");
        }

    }
}
