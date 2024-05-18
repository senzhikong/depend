package com.senzhikong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shu
 */
@Data
@Schema(name = "配置列表")
public class ConfigListResult implements Serializable {
    private String title;
    private List<ConfigListResult> children;
    private List<ConfigItemDTO> configs;

    public ConfigListResult addChildList(String title) {
        if (children == null) {
            children = new ArrayList<>();
        }
        ConfigListResult res = new ConfigListResult();
        res.setTitle(title);
        children.add(res);
        return res;
    }

    public void addConfigItem(BaseConfigConstants constants, AbstractConfigInterface config) {
        if (configs == null) {
            configs = new ArrayList<>();
        }
        configs.add(new ConfigItemDTO(constants, config));
    }
}
