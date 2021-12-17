package com.senzhikong.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Shu.zhou
 * @date 2018年12月7日下午3:07:14
 */
public abstract class ConfigInterface {

    public abstract void reloadConfig();

    public abstract String getConfigValue(String key);

    public abstract String getConfigValue(String key, String defaultValue);

    public abstract void updateConfig(String key, Object value);

    public abstract void publishConfig(Map<String, String> configMap);

    public Integer getIntConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Integer.parseInt(configValue) : null;
    }


    public Double getDoubleConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Double.parseDouble(configValue) : null;
    }


    public Boolean getBooleanConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Boolean.parseBoolean(configValue) : null;
    }

    public JSONObject getJSONObject(String key) {
        String longtext = getConfigValue(key);
        return key == null ? null : JSONObject.parseObject(longtext);
    }


    public JSONArray getJSONArray(String key) {
        String longtext = getConfigValue(key);
        return key == null ? null : JSONArray.parseArray(longtext);
    }


    public Long getLongConfig(String key) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Long.parseLong(configValue) : null;
    }


    public Boolean getBooleanConfig(String key, boolean defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Boolean.parseBoolean(configValue) : defaultValue;
    }


    public Integer getIntConfig(String key, int defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Integer.parseInt(configValue) : defaultValue;
    }


    public Double getDoubleConfig(String key, double defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Double.parseDouble(configValue) : defaultValue;
    }


    public Long getLongConfig(String key, long defaultValue) {
        String configValue = getConfigValue(key);
        return StringUtils.isNotEmpty(configValue) ? Long.parseLong(configValue) : defaultValue;
    }


    public JSONObject getJSONObject(String key, JSONObject defaultValue) {
        JSONObject res = getJSONObject(key);
        return res == null ? defaultValue : res;
    }


    public JSONArray getJSONArray(String key, JSONArray defaultValue) {
        JSONArray res = getJSONArray(key);
        return res == null ? defaultValue : res;
    }
}
