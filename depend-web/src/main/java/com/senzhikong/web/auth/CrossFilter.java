package com.senzhikong.web.auth;

import com.senzhikong.web.util.CrossUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Slf4j
//@Order(1)
//@Component
//@WebFilter(filterName = "crossFilter", urlPatterns = "/*")
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
//        CrosUtil.cors(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
