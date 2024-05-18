package com.senzhikong.bean;

import com.alibaba.fastjson.JSONArray;
import lombok.NonNull;


/**
 * @author shu
 */
public class JsonArr extends JSONArray {
    public @NonNull JsonArr addItem(Object value) {
        super.add(value);
        return this;
    }

    public @NonNull JsonObj addObj() {
        JsonObj obj = new JsonObj();
        this.add(obj);
        return obj;
    }

    public @NonNull JsonArr addArr(String key) {
        JsonArr arr = new JsonArr();
        this.add(arr);
        return arr;
    }
}
