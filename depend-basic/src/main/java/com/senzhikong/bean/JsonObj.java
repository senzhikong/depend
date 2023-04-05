package com.senzhikong.bean;


import com.alibaba.fastjson.JSONObject;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * @author shu.zhou
 */
public class JsonObj extends JSONObject {
    @Override
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
}
