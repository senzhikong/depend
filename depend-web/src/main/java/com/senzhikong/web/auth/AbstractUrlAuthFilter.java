package com.senzhikong.web.auth;

import com.senzhikong.web.ajax.ApiResponse;
import com.senzhikong.web.ajax.ApiStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shu
 */
public abstract class AbstractUrlAuthFilter extends BaseFilter {
    private static final String ANNO = "anno";

    /**
     * 初始化过滤器
     */
    public abstract void initFilter();

    @Override
    public void init(FilterConfig filterConfig) {
        initFilter();
    }

    /**
     * 获取所有过滤连
     *
     * @return 过滤链Map
     */
    public abstract Map<String, UrlFilterChain> getChainMap();

    /**
     * 获取所有过滤键
     *
     * @return 过滤键
     */
    public abstract List<String> getFilterKeys();

    /**
     * 登录校验
     *
     * @param servletRequest http请求
     * @return 登录账号
     */
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
        if (filters.contains(ANNO)) {
            chain.doFilter(request, response);
            return;
        } else if (!filters.contains(ANNO) && loginAccountModel == null) {
            reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_LOGIN));
            return;
        }
        List<String> permissions = urlFilterChain.getPermissions();
        List<String> roles = urlFilterChain.getRoles();

        if (permissions != null && permissions.size() > 0) {
            List<String> accountPermission = loginAccountModel.getPermission();
            if (!new HashSet<>(accountPermission).containsAll(permissions)) {
                reject(servletResponse, new ApiResponse<>(ApiStatus.NOT_PERMISSION));
                return;
            }
        }
        if (roles != null && roles.size() > 0) {
            List<String> accountRole = loginAccountModel.getRoles();
            if (!new HashSet<>(accountRole).containsAll(roles)) {
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
