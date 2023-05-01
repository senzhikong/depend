package com.senzhikong.cache;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.cache.manager.BaseCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
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
        log.debug("Ready To Clean Cache：" + cacheName);
        Objects.requireNonNull(cacheManager.getCache(cacheName))
                .clear();
        log.debug("Clean Cache Success：" + cacheName);
    }

    private IBaseCache getCache(String cacheName) {
        return cacheManager.getOrCreateCache(cacheName);
    }

    public Object findCache(String cacheName, String key) {
        IBaseCache cache = getCache(cacheName);
        log.debug(String.format("Looking For Cache：%s:::%s", cacheName, key));
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper value = cache.get(key);
        Object val = value == null ? null : value.get();
        log.debug(String.format("Looking Cache Result：%s", val));
        return val;
    }

    public <T> T findCache(String cacheName, String key, Class<T> clz) {
        Cache cache = getCache(cacheName);
        log.debug(String.format("Looking For Cache：%s:::%s", cacheName, key));
        if (cache == null) {
            return null;
        }
        T val = cache.get(key, clz);
        log.debug(String.format("Looking For Cache：%s", val));
        return val;
    }

    public void saveCache(String cacheName, String key, Object value) {
        saveCache(cacheName, key, value, null);
    }

    public void saveCache(String cacheName, String key, Object value, Duration duration) {
        IBaseCache cache = getCache(cacheName);
        log.debug(String.format("Save Cache：%s:::%s", cacheName, key));
        log.debug(String.format("Save Cache Content：%s", value));
        if (cache == null) {
            return;
        }
        if (value == null) {
            removeCache(key);
        } else {
            cache.put(key, value, duration);
        }
    }

    public void removeCache(String cacheName, String... keys) {
        IBaseCache cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        for (String key : keys) {
            log.debug(String.format("Remove Cache：%s:::%s", cacheName, key));
            cache.evict(key);
        }
    }
}
