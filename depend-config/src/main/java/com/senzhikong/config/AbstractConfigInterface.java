package com.senzhikong.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Shu.zhou
 * @date 2018年12月7日下午3:07:14
 */
public abstract class AbstractConfigInterface {
    /**
     * 重载系统配置
     */
    public abstract void reloadConfig();

    /**
     * 获取配置值
     *
     * @param key 配置编码
     * @return 配置值
     */
    public abstract String getConfigValue(String key);

    /**
     * 获取配置值
     *
     * @param key          配置编码
     * @param defaultValue 默认值
     * @return 配置值
     */
    public abstract String getConfigValue(String key, String defaultValue);

    /**
     * 更新配置
     *
     * @param key   配置编码
     * @param value 配置值
     */
    public abstract void updateConfig(String key, Object value);

    /**
     * 发布配置
     *
     * @param configMap 发布配置
     */
    public abstract void publishConfig(Map<String, String> configMap);

    /**
     * 获取整型配置
     *
     * @param key 配置编号
     * @return 配置值
     */
    public Integer getIntConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Integer.parseInt(configValue) : null;
    }


    /**
     * 获取浮点配置
     *
     * @param key 配置编号
     * @return 配置值
     */
    public Double getDoubleConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Double.parseDouble(configValue) : null;
    }


    /**
     * 获取布尔配置
     *
     * @param key 配置编号
     * @return 配置值
     */
    public Boolean getBooleanConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Boolean.parseBoolean(configValue) : null;
    }

    /**
     * 获取JSON对象配置
     *
     * @param key 配置编号
     * @return 配置值
     */
    public JSONObject getJsonObject(String key) {
        String longtext = getConfigValue(key);
        return key == null ? null : JSONObject.parseObject(longtext);
    }


    /**
     * 获取JSON数组配置
     *
     * @param key 配置编号
     * @return 配置值
     */
    public JSONArray getJsonArray(String key) {
        String longtext = getConfigValue(key);
        return key == null ? null : JSONArray.parseArray(longtext);
    }


    /**
     * 获取长整型配置
     *
     * @param key 配置编号
     * @return 配置值
     */
    public Long getLongConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Long.parseLong(configValue) : null;
    }


    /**
     * 获取布尔配置
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @return 配置值
     */
    public Boolean getBooleanConfig(String key, boolean defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Boolean.parseBoolean(configValue) : defaultValue;
    }


    /**
     * 获取整型配置
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @return 配置值
     */
    public Integer getIntConfig(String key, int defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Integer.parseInt(configValue) : defaultValue;
    }


    /**
     * 获取浮点配置
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @return 配置值
     */
    public Double getDoubleConfig(String key, double defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Double.parseDouble(configValue) : defaultValue;
    }


    /**
     * 获取长整型配置
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @return 配置值
     */
    public Long getLongConfig(String key, long defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Long.parseLong(configValue) : defaultValue;
    }


    /**
     * 获取JSON对象配置
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @return 配置值
     */
    public JSONObject getJsonObject(String key, JSONObject defaultValue) {
        JSONObject res = getJsonObject(key);
        return res == null ? defaultValue : res;
    }


    /**
     * 获取JOSN数组配置
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @return 配置值
     */
    public JSONArray getJsonArray(String key, JSONArray defaultValue) {
        JSONArray res = getJsonArray(key);
        return res == null ? defaultValue : res;
    }
}
