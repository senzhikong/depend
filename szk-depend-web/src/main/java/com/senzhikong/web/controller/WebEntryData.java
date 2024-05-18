package com.senzhikong.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.util.EnumUtil;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author shu.zhou
 */
public class WebEntryData {
    public JSONArray search = new JSONArray();
    public JSONObject detail = new JSONObject();

    public void addSearch(JSONObject obj) {
        search.add(obj);
    }

    public void addTextSearch(String name, String code) {
        JSONObject s = new JSONObject();
        s.put("type", "text");
        s.put("name", name);
        s.put("code", code);
        search.add(s);
    }

    public void addBooleanSearch(String name, String code, String trueLabel, String falseLabel) {
        JSONArray booleanList = new JSONArray();
        JSONObject trueOpt = new JSONObject();
        trueOpt.put("code", true);
        trueOpt.put("description", trueLabel);
        booleanList.add(trueOpt);
        JSONObject falseOpt = new JSONObject();
        falseOpt.put("code", false);
        falseOpt.put("description", falseLabel);
        booleanList.add(falseOpt);
        this.addSelectSearch(name, code, "description", "code", booleanList);
    }

    public void addDateTimeSearch(String name, String code, String format) {
        JSONObject s = new JSONObject();
        s.put("type", "datetime");
        s.put("name", name);
        s.put("code", code);
        s.put("format", format);
        search.add(s);
    }

    public void addDateTimeRangeSearch(String name, String code, String format) {
        JSONObject s = new JSONObject();
        s.put("type", "datetime");
        s.put("name", name);
        s.put("code", code);
        s.put("format", format);
        s.put("range", true);
        search.add(s);
    }

    public void addSelectSearch(String name, String code, Class<? extends Enum<?>> clz) {
        this.addSelectSearch(name, code, "description", "code", EnumUtil.toJsonArray(clz));
    }

    public void addSelectSearch(String name, String code, String nameCol, String codeCol, Object[] data) {
        this.addSelectSearch(name, code, nameCol, codeCol, Arrays.asList(data));
    }

    public void addSelectSearch(String name, String code, String nameCol, String codeCol, Collection<?> data) {
        JSONObject s = new JSONObject();
        s.put("type", "select");
        s.put("name", name);
        s.put("code", code);
        s.put("nameCol", nameCol);
        s.put("codeCol", codeCol);
        s.put("data", data);
        search.add(s);
    }

    public void addTreeSearch(String name, String code, String nameCol, String codeCol, String children,
            Collection<?> data) {
        JSONObject s = new JSONObject();
        s.put("type", "tree");
        s.put("name", name);
        s.put("code", code);
        s.put("nameCol", nameCol);
        s.put("codeCol", codeCol);
        s.put("children", children);
        s.put("data", data);
        search.add(s);
    }

    public void addDetail(String name, String code, Object data) {
        JSONObject s = new JSONObject();
        s.put("name", name);
        s.put("code", code);
        s.put("data", data);
        detail.put(code, s);
    }

    public void addSelectDetail(String name, String code, Class<? extends Enum<?>> clz) {
        this.addSelectDetail(name, code, "description", "code", EnumUtil.toJsonArray(clz));
    }

    public void addSelectDetail(String name, String code, String nameCol, String codeCol, Object[] data) {
        this.addSelectDetail(name, code, nameCol, codeCol, Arrays.asList(data));
    }

    public void addSelectDetail(String name, String code, String nameCol, String codeCol, Collection<?> data) {
        JSONObject s = new JSONObject();
        s.put("type", "select");
        s.put("name", name);
        s.put("code", code);
        s.put("nameCol", nameCol);
        s.put("codeCol", codeCol);
        s.put("data", data);
        detail.put(code, s);
    }

    public void addTreeDetail(String name, String code, String nameCol, String codeCol, String children,
            Collection<?> data) {
        JSONObject s = new JSONObject();
        s.put("type", "tree");
        s.put("name", name);
        s.put("code", code);
        s.put("nameCol", nameCol);
        s.put("codeCol", codeCol);
        s.put("children", children);
        s.put("data", data);
        detail.put(code, s);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public JSONArray getSearch() {
        return search;
    }

    public void setSearch(JSONArray search) {
        this.search = search;
    }

    public JSONObject getDetail() {
        return detail;
    }

    public void setDetail(JSONObject detail) {
        this.detail = detail;
    }
}
