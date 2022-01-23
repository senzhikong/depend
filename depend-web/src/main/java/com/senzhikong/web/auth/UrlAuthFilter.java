package com.senzhikong.web.auth;

import com.senzhikong.web.ajax.ApiResponse;
import com.senzhikong.web.ajax.ApiStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UrlAuthFilter extends BaseFilter {

    public abstract void initFilter();

    @Override
    public void init(FilterConfig filterConfig) {
        initFilter();
    }

    public abstract Map<String, UrlFilterChain> getChainMap();

    public abstract List<String> getFilterKeys();

    public abstract LoginAccountModel doLoginAuth(HttpServletRequest servletRequest);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String reqUrl = servletRequest.getRequestURI().toLowerCase().trim();
        Map<String, UrlFilterChain> chainMap = getChainMap();
        List<String> filterChainKeys = getFilterKeys();
        UrlFilterChain urlFilterChain = chainMap.get(reqUrl);
        if (!chainMap.containsKey(reqUrl)) {
            for (String filterKey : filterChainKeys) {
                Pattern pattern = Pattern.compile(filterKey);
                Matcher matcher = pattern.matcher(reqUrl);
                if (!matcher.matches()) {
                    continue;
                }
                urlFilterChain = chainMap.get(filterKey);
                break;
            }
            chainMap.put(reqUrl, urlFilterChain);
        }
        LoginAccountModel loginAccountModel = doLoginAuth(servletRequest);
        if (urlFilterChain == null) {
            chain.doFilter(request, response);
            return;
        }
        List<String> filters = urlFilterChain.getFilter();
        //登录校验
        if (filters.contains("auth") && loginAccountModel == null) {
            reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_LOGIN, "用户未登录"));
            return;
        }
        if (!filters.contains("anon") && loginAccountModel == null) {
            reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_LOGIN, "不允许匿名访问"));
            return;
        }
        List<String> permissions = urlFilterChain.getPermissions();
        List<String> roles = urlFilterChain.getRoles();

        if (loginAccountModel == null && (permissions.size() > 0 || roles.size() > 0)) {
            reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_PERMISSION));
            return;
        }
        if (permissions != null && permissions.size() > 0) {
            List<String> accountPermission = loginAccountModel.getPermission();
            if (!accountPermission.containsAll(permissions)) {
                reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_PERMISSION));
                return;
            }
        }
        if (roles != null && roles.size() > 0) {
            List<String> accountRole = loginAccountModel.getRoles();
            if (!accountRole.containsAll(roles)) {
                reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_PERMISSION));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
