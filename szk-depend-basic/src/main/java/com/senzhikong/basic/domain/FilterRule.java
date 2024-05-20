package com.senzhikong.basic.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author shu.zhou
 */
@Data
@Slf4j
public class FilterRule implements Serializable {
    private String encType;
    private boolean anonymous = false;
    private List<String> permissions;
    private List<String> roles;

    public FilterRule(String rule, String encType) {
        if (StringUtils.isBlank(rule)) {
            return;
        }
        JSONObject json;
        try {
            json = JSONObject.parseObject(rule);
        } catch (Exception exception) {
            log.error("拦截规则解析失败：" + rule);
            return;
        }
        this.anonymous = json.getBooleanValue("anonymous");
        JSONArray permissions = json.getJSONArray("permission");
        JSONArray roles = json.getJSONArray("role");
        if (permissions == null) {
            permissions = new JSONArray();
        }
        if (roles == null) {
            roles = new JSONArray();
        }
        this.permissions = permissions.toJavaList(String.class);
        this.roles = roles.toJavaList(String.class);
        this.encType = encType;
    }
}
