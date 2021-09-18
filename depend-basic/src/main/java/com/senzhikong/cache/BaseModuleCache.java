package com.senzhikong.cache;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.cache.manager.BaseCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public abstract class BaseModuleCache {
    @Resource
    private BaseCacheManager cacheManager;

    public abstract String getCacheName();

    public void cleanCache() {
        log.debug("Ready To Clean Cache：" + getCacheName());
        Objects.requireNonNull(getCache()).clear();
        log.debug("Clean Cache Success：" + getCacheName());
    }

    private IBaseCache getCache() {
        return cacheManager.getOrCreateCache(getCacheName());
    }

    public Object findCache(String key) {
        IBaseCache cache = getCache();
        log.debug(String.format("Looking For Cache：%s:::%s", getCacheName(), key));
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper value = cache.get(key);
        Object val = value == null ? null : value.get();
        log.debug(String.format("Looking Cache Result：%s", val));
        return val;
    }

    public <T> T findCache(String key, Class<T> clz) {
        Cache cache = getCache();
        log.debug(String.format("Looking For Cache：%s:::%s", getCacheName(), key));
        if (cache == null) {
            return null;
        }
        T val = cache.get(key, clz);
        log.debug(String.format("Looking For Cache：%s", val));
        return val;
    }

    public void saveCache(String key, Object value) {
        IBaseCache cache = getCache();
        log.debug(String.format("Save Cache：%s:::%s", getCacheName(), key));
        log.debug(String.format("Save Cache Content：%s", value));
        if (cache == null) {
            return;
        }
        if (value == null) {
            removeCache(key);
        } else {
            cache.put(key, value);
        }
    }

    public void saveCache(String key, Object value, Duration duration) {
        IBaseCache cache = getCache();
        log.debug(String.format("Save Cache：%s:::%s", getCacheName(), key));
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

    public void removeCache(String... keys) {
        IBaseCache cache = getCache();
        if (cache == null) {
            return;
        }
        for (String key : keys) {
            log.debug(String.format("Remove Cache：%s:::%s", getCacheName(), key));
            cache.evict(key);
        }
    }
}
