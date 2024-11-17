package com.senzhikong.auth.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author shu.zhou
 */
@Data
public class FilterRule implements Serializable {
    private String encType;
    private boolean anonymous = false;
    private List<String> permissions;
    private List<String> roles;

    public static FilterRule from(String rule, String encType) {
        FilterRule filterRule = new FilterRule();
        if (StringUtils.isBlank(rule)) {
            return filterRule;
        }
        JSONObject json;
        try {
            json = JSONObject.parseObject(rule);
        } catch (Exception exception) {
            throw new RuntimeException("拦截规则解析失败：" + rule);
        }
        filterRule.setAnonymous(json.getBooleanValue("anonymous"));
        JSONArray permissions = json.getJSONArray("permission");
        JSONArray roles = json.getJSONArray("role");
        if (permissions == null) {
            permissions = new JSONArray();
        }
        if (roles == null) {
            roles = new JSONArray();
        }
        filterRule.setPermissions(permissions.toJavaList(String.class));
        filterRule.setRoles(roles.toJavaList(String.class));
        filterRule.setEncType(encType);
        return filterRule;
    }
}
