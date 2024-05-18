package com.senzhikong.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author shu
 */
@Slf4j
@Component
public class NacosConfig {
    private static final Map<String, String> CONFIG_MAP = new HashMap<>();
    @Resource
    private NacosConfigManager nacosConfigManager;

    private ConfigService configService;

    @Bean
    ConfigService nacosConfigService() {
        configService = nacosConfigManager.getConfigService();
        return configService;
    }

    @Value("${szk.config.group:DynamicConfig}")
    private String group;

    static class ConfigListener implements Listener {
        private final String configCode;

        public ConfigListener(String configCode) {
            this.configCode = configCode;
        }

        @Override
        public Executor getExecutor() {
            return null;
        }

        @Override
        public void receiveConfigInfo(String s) {
            log.info("\n配置更新：{}\n旧配置：{}\n新配置：{}\n", configCode, CONFIG_MAP.get(configCode), s);
            CONFIG_MAP.put(configCode, s);
        }
    }

    public void reloadConfig() {
        CONFIG_MAP.keySet().forEach(configCode -> {
            String group = configCode.substring(0, configCode.indexOf("."));
            String configValue = null;
            try {
                configValue = configService.getConfig(configCode, group, 1000L);
            } catch (NacosException e) {
                log.error(e.getMessage(), e);
            }
            CONFIG_MAP.put(configCode, configValue);
        });
    }

    public String getConfigValue(String key) {
        return getConfigValue(key, null);
    }

    public String getConfigValue(String key, String defaultValue) {
        if (!CONFIG_MAP.containsKey(key)) {
            try {
                String configValue = configService.getConfig(key, group, 1000L);
                log.info("获取配置：{}={}", key, configValue);
                listenConfig(key);
                CONFIG_MAP.put(key, configValue);
            } catch (NacosException e) {
                log.error(e.getMessage(), e);
            }
        }
        return CONFIG_MAP.getOrDefault(key, defaultValue);
    }

    public void publishConfig(Map<String, String> configMap) {
        configMap.keySet().forEach(key -> {
            String configValue = configMap.get(key);
            try {
                configService.publishConfig(key, group, configValue);
            } catch (NacosException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void publishConfig(String configCode, String configValue) {
        try {
            if (StringUtils.isEmpty(configValue)) {
                log.info("移除配置：{}", configCode);
                configService.removeConfig(configCode, group);
            } else {
                log.info("发布配置：{}={}", configCode, configValue);
                configService.publishConfig(configCode, group, configValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void removeConfig(String configCode) {
        this.publishConfig(configCode, null);
    }

    public void listenConfig(String configCode) {
        try {
            log.info("监听配置：{}", configCode);
            configService.addListener(configCode, group, new ConfigListener(configCode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
