package com.senzhikong.locale.cache;

import com.alibaba.fastjson.JSONObject;
import com.senzhikong.basic.dto.BaseApiResp;
import com.senzhikong.cache.BaseModuleCache;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author shu.zhou
 */
@Slf4j
@Getter
@Component
@ConditionalOnProperty(prefix = "szk.locale", name = "type", havingValue = "cache")
public class LocaleCache extends BaseModuleCache {
    private final String cacheName = "locale";
    private final Integer redisExpireMinute = 10;

    @Resource
    private RestTemplate restTemplate;
    @Value("${szk.locale.feign-url}")
    private String feignUrl;

    public String findLocaleString(String code, String locale) {
        String group = code.substring(0, code.lastIndexOf(":"));
        String codeKey = code.substring(code.lastIndexOf(":") + 1);
        CacheLocaleMap localeMap = findLocaleFromCache(group, locale);
        if (localeMap == null) {
            localeMap = findLocaleFromFeign(group, locale);
        }
        if (localeMap == null) {
            return code;
        }
        return localeMap.getOrDefault(codeKey, code);
    }

    private CacheLocaleMap findLocaleFromCache(String group, String locale) {
        String cacheKey = String.format("%s-%s", locale, group.replaceAll("\\.", "-"));
        return findCache(cacheKey, CacheLocaleMap.class);
    }

    private CacheLocaleMap findLocaleFromFeign(String group, String locale) {
        JSONObject param = new JSONObject();
        param.put("group", group);
        param.put("locale", locale);
        try {
            ResponseEntity<BaseApiResp> response = restTemplate.postForEntity(feignUrl, param, BaseApiResp.class);
            if (response.getBody() == null) {
                throw new RuntimeException(String.valueOf(response.getStatusCode().value()));
            }
            if (!response.getBody().success()) {
                throw new RuntimeException(response.getBody().getMessage());
            }
            if (response.getBody().getData() == null) {
                throw new RuntimeException("返回多语言为空");
            }
            CacheLocaleMap localeMap = response.getBody().getData(CacheLocaleMap.class);
            String cacheKey = String.format("%s-%s", locale, group.replaceAll("\\.", "-"));
            saveCache(cacheKey, localeMap, Duration.ofMinutes(redisExpireMinute));
            return localeMap;
        } catch (Exception e) {
            log.error("获取多语言失败{}，{}", group, locale, e);
        }
        return null;
    }
}
