package com.senzhikong.web.auth;

import lombok.Data;

import java.util.List;

/**
 * @author shu
 */
@Data
public class UrlFilterChain {
    private String pattern;
    private List<String> filter;
    private List<String> permissions;
    private List<String> roles;
    private List<Long> roleIds;
    private List<Long> permissionIds;
}
