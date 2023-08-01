package com.senzhikong.web.auth;

import com.senzhikong.web.util.CrossUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author shu
 */
public class CrossFilter extends BaseFilter {
    @Value("${szk.filter.cross}")
    private boolean cross;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (cross) {
            CrossUtil.cors(servletRequest, servletResponse);
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void destroy() {

    }
}
