package com.senzhikong.web.util;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Lazy
@Component
public class WebUtil {
    @Resource
    private RequestMappingHandlerMapping handlerMapping;

    public List<String> getAllApi() {
        List<String> urls = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> map = this.handlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> set = map.keySet();
        for (RequestMappingInfo info : set) {
            String reqURIs = info.getPatternsCondition().toString();
            String url = reqURIs.substring(1, reqURIs.length() - 1);
            urls.add(url);
        }
        return urls;
    }
}
