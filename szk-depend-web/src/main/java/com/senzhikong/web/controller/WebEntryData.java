package com.senzhikong.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.util.EnumUtil;
import com.senzhikong.util.enums.BaseEnum;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author shu.zhou
 */
public class WebEntryData {
    public JSONObject json = new JSONObject();

    public void addSelect(String label, String value, Class<? extends BaseEnum> clz) {
        this.addSelect(label, value, "label", "value", EnumUtil.toJsonArray(clz));
    }

    public void addSelect(String label, String value, String labelCol, String valueCol, Object[] data) {
        this.addSelect(label, value, labelCol, valueCol, Arrays.asList(data));
    }

    public void addSelect(String label, String value, String labelCol, String valueCol, Collection<?> data) {
        JSONObject s = new JSONObject();
        s.put("type", "select");
        s.put("label", label);
        s.put("value", value);
        s.put("labelCol", labelCol);
        s.put("valueCol", valueCol);
        s.put("data", data);
        json.put(value, s);
    }

    public void addTree(String label, String value, String labelCol, String valueCol, String children,
                        Collection<?> data) {
        JSONObject s = new JSONObject();
        s.put("type", "tree");
        s.put("label", label);
        s.put("value", value);
        s.put("labelCol", labelCol);
        s.put("valueCol", valueCol);
        s.put("children", children);
        s.put("data", data);
        json.put(value, s);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
