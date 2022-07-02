package com.senzhikong.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.senzhikong.spring.SpringContextHolder;
import com.senzhikong.util.string.StringUtil;
import com.senzhikong.module.InitializeBean;
import com.senzhikong.module.Module;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author shu
 */
@Getter
@Setter
@Slf4j
public class NacosConfig extends AbstractConfigInterface implements InitializeBean {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosServerAddress;
    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String nacosNamespace;
    @Value("${szk.config.publish}")
    private Boolean publish;
    private static final Map<String, String> CONFIG_MAP = new HashMap<>();
    private ConfigService configService;

    @Override
    public void init() {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", nacosServerAddress);
        properties.setProperty("namespace", nacosNamespace);
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        initConfig();
        reloadConfig();
    }

    void initConfig() {
        Map<String, Module> moduleBeanMap = SpringContextHolder.getApplicationContext().getBeansOfType(
                 Module.class);
        List<BaseConfigConstants> configConstantsList = new ArrayList<>();
        for (Module module : moduleBeanMap.values()) {
            configConstantsList.addAll(Arrays.asList(module.getConfigConstants()));
        }
        configConstantsList.forEach(item -> {
            CONFIG_MAP.put(item.getCode(), null);
            listenConfig(item.getCode());
        });
    }

    static class ConfigListener implements Listener {
        private final String configName;

        public ConfigListener(String configName) {
            this.configName = configName;
        }

        @Override
        public Executor getExecutor() {
            return null;
        }

        @Override
        public void receiveConfigInfo(String s) {
            log.info("\n配置更新：{}\n旧配置：{}\n新配置：{}\n", configName, CONFIG_MAP.get(configName), s);
            CONFIG_MAP.put(configName, s);
        }
    }

    @Override
    public void reloadConfig() {
        CONFIG_MAP.keySet().forEach(configName -> {
            String group = configName.substring(0, configName.indexOf("."));
            String configValue = null;
            try {
                configValue = configService.getConfig(configName, group, 1000L);
            } catch (NacosException e) {
                e.printStackTrace();
            }
            CONFIG_MAP.put(configName, configValue);
        });
    }

    @Override
    public String getConfigValue(String key) {
        return CONFIG_MAP.get(key);
    }

    @Override
    public String getConfigValue(String key, String defaultValue) {
        return CONFIG_MAP.getOrDefault(key, defaultValue);
    }

    @Override
    public void publishConfig(Map<String, String> configMap) {
        configMap.keySet().forEach(configName -> {
            String group = configName.substring(0, configName.indexOf("."));
            String configValue = configMap.get(configName);
            try {
                configService.publishConfig(configName, group, configValue);
            } catch (NacosException e) {
                e.printStackTrace();
            }
            configMap.put(configName, configValue);
        });
    }

    public void publishConfig(String configName, String configValue) {
        String group = configName.substring(0, configName.indexOf("."));
        try {
            if (StringUtil.isEmpty(configValue)) {
                configService.removeConfig(configName, group);
            } else {
                configService.publishConfig(configName, group, configValue);
            }
        } catch (Exception e) {
            System.out.println(configName + ">>>" + configValue);
            e.printStackTrace();
        }
    }

    public void listenConfig(String configName) {
        String group = configName.substring(0, configName.indexOf("."));
        try {
            configService.addListener(configName, group, new ConfigListener(configName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateConfig(String key, Object value) {
        publishConfig(key, String.valueOf(value));
    }

}
