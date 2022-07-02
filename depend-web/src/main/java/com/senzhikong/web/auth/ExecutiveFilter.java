package com.senzhikong.web.auth;

import com.senzhikong.web.ajax.ApiResponse;
import com.senzhikong.web.ajax.ApiStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shu
 */
@Slf4j
@Order(10)
@Component
@WebFilter(filterName = "execFilter", urlPatterns = "/*")
public class ExecutiveFilter extends BaseFilter {

    protected static String[] blackUrlPathPattern =
            new String[]{"*.aspx", "*.asp", "*.php", "*.exe", "*.jsp", "*.pl", "*.py", "*.groovy", "*.sh",
                    "*.rb", "*.dll", "*.bat", "*.bin", "*.dat", "*.bas", "*.c", "*.cmd", "*.com", "*.cpp",
                    "*.jar", "*.class", "*.lnk"};


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String reqUrl = servletRequest.getRequestURI()
                .toLowerCase()
                .trim();
        for (String pattern : blackUrlPathPattern) {
            if (PatternMatchUtils.simpleMatch(pattern, reqUrl)) {
                log.error("unsafe request >>> " +
                        " request time: " +
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                        "; request ip: " +
                        request.getRemoteAddr() +
                        "; request url: " +
                        reqUrl);
                reject((HttpServletResponse) response, new ApiResponse<>(ApiStatus.FORBIDDEN, "不安全的请求，禁止访问！"));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
