package com.senzhikong.module;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public interface BaseConfigConstants extends Serializable {
    public String code();

    public String description();

    public String getCode();

    public void setCode(String code);

    public String getDescription();

    public void setDescription(String description);

    public String getType();

    public void setType(String type);

    public boolean getSecret();

    public void setSecret(boolean secret);

    public Object getData();

    public void setData(Object data);

    public default JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("code", getCode());
        json.put("description", getDescription());
        json.put("type", getType());
        json.put("secret", getSecret());
        json.put("data", getData());
        return json;
    }
}
