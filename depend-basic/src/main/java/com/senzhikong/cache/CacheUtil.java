package com.senzhikong.cache;

import com.senzhikong.cache.manager.BaseCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Shu.Zhou
 */
@Component
@Slf4j
public class CacheUtil {
    @Resource
    private BaseCacheManager cacheManager;

    public void cleanCache(String cacheName) {
        log.debug("准备清空缓存：" + cacheName);
        Objects.requireNonNull(cacheManager.getCache(cacheName))
                .clear();
        log.debug("成功清空缓存：" + cacheName);
    }

    private Cache getCache(String cacheName) {
        return cacheManager.getOrCreateCache(cacheName);
    }

    public Object findCache(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        log.debug(String.format("查找缓存：%s:::%s", cacheName, key));
        if (cache == null) {
            return null;
        }
        ValueWrapper value = cache.get(key);
        Object val = value == null ? null : value.get();
        log.debug(String.format("查询结果：%s", val));
        return val;
    }

    public <T> T findCache(String cacheName, String key, Class<T> clz) {
        Cache cache = getCache(cacheName);
        log.debug(String.format("查找缓存：%s:::%s", cacheName, key));
        if (cache == null) {
            return null;
        }
        T val = cache.get(key, clz);
        log.debug(String.format("查询结果：%s", val));
        return val;
    }

    public void saveCache(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        log.debug(String.format("添加缓存：%s:::%s", cacheName, key));
        log.debug(String.format("缓存内容：%s", value));
        if (cache == null) {
            return;
        }
        if (value == null) {
            removeCache(cacheName, key);
        } else {
            cache.put(key, value);
        }
    }

    public void removeCache(String cacheName, String... keys) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        for (String key : keys) {
            log.debug(String.format("移除缓存：%s:::%s", cacheName, key));
            cache.evict(key);
        }
    }
}
