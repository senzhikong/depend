package com.senzhikong.bean;

import com.alibaba.fastjson.JSONArray;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * @author shu
 */
public class JsonArr extends JSONArray {
    public @NonNull JsonArr addItem(Object value) {
        super.add(value);
        return this;
    }

    public @NotNull JsonObj addObj(String key) {
        JsonObj obj = new JsonObj();
        this.add(obj);
        return obj;
    }

    public @NotNull JsonArr addArr(String key) {
        JsonArr arr = new JsonArr();
        this.add(arr);
        return arr;
    }
}
