package com.senzhikong.dto;

import com.senzhikong.module.BaseConfigConstants;
import com.senzhikong.module.ConfigInterface;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("配置列表")
public class ConfigListResult {
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
