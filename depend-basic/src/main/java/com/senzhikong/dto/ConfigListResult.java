package com.senzhikong.dto;

import com.senzhikong.config.BaseConfigConstants;
import com.senzhikong.config.ConfigInterface;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("配置列表")
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

    public void addConfigItem(BaseConfigConstants constants, ConfigInterface config) {
        if (configs == null) {
            configs = new ArrayList<>();
        }
        configs.add(new ConfigItemDTO(constants, config));
    }
}
