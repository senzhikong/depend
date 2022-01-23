package com.senzhikong.config;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public interface BaseConfigConstants extends Serializable {
    String getCode();

    String getDescription();

    String getType();

    boolean isSecret();

    Object getData();

      default String code() {
        return getCode();
    }

      default String description() {
        return getDescription();
    }

    default JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("code", getCode());
        json.put("description", getDescription());
        json.put("type", getType());
        json.put("secret", isSecret());
        json.put("data", getData());
        return json;
    }
}
