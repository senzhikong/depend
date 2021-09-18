package com.senzhikong.bean;


import com.alibaba.fastjson.JSONObject;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

public class JsonObj extends JSONObject {
    public @NonNull JsonObj put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public @NotNull JsonObj putObj(String key) {
        JsonObj obj = new JsonObj();
        this.put(key, obj);
        return obj;
    }

    public @NotNull JsonArr putArr(String key) {
        JsonArr arr = new JsonArr();
        this.put(key, arr);
        return arr;
    }

    public static void main(String[] args) {
        JsonObj json = new JsonObj()
                .put("test1", "dafasdfasdf")
                .put("dasfasdf", "大发送到发");
        json.putObj("child")
                .put("hhad", "大大")
                .putObj("asdfasd")
                .put("oooo", "312312");
        System.out.println(json.toString());
    }
}
