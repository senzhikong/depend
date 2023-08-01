package com.senzhikong.web.util;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shu
 */
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
            assert info.getPatternsCondition() != null;
            String reqUris = info.getPatternsCondition().toString();
            String url = reqUris.substring(1, reqUris.length() - 1);
            urls.add(url);
        }
        return urls;
    }
}
