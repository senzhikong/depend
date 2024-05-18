package com.senzhikong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "配置节点")
public class ConfigItemDTO implements Serializable {
    private String code;
    private String description;
    private String type;
    private Boolean secret;
    private Object data;
    private Object value;

    public ConfigItemDTO(BaseConfigConstants constants, AbstractConfigInterface config) {
        this.code = constants.getCode();
        this.description = constants.getDescription();
        this.type = constants.getType();
        this.secret = constants.isSecret();
        this.data = constants.getData();
        this.value = "";
        if (secret) {
            return;
        }
        if (BaseConfigConstants.LONGTEXT.equals(type)) {
            this.value = config.getConfigValue(this.code, "");
        } else if (BaseConfigConstants.NUMBER.equals(type)) {
            if (StringUtils.isNotBlank(config.getConfigValue(this.code))) {
                this.value = new BigDecimal(config.getConfigValue(this.code));
            }
        } else if (BaseConfigConstants.BOOLEAN.equals(type)) {
            this.value = config.getBooleanConfig(this.code, false);
        } else if (BaseConfigConstants.JSON.equals(type)) {
            this.value = config.getConfigValue(this.code, "{}");
        } else {
            this.value = config.getConfigValue(this.code, "");
        }

    }
}
