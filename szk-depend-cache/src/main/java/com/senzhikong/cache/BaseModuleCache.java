package com.senzhikong.cache;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * @author shu
 */
@Slf4j
public abstract class BaseModuleCache {
    @Resource
    private CacheUtil cacheUtil;

    /**
     * 获取模块名称
     *
     * @return 模块名称
     */
    public abstract String getCacheName();

    public void cleanCache() {
        cacheUtil.cleanCache(getCacheName());
    }


    public Object findCache(String key) {
        return cacheUtil.findCache(getCacheName(), key);
    }

    public <T> T findCache(String key, Class<T> clz) {
        return cacheUtil.findCache(getCacheName(), key, clz);
    }

    public void saveCache(String key, Object value) {
        cacheUtil.saveCache(getCacheName(), key, value);
    }

    public void saveCache(String key, Object value, Duration duration) {
        cacheUtil.saveCache(getCacheName(), key, value, duration);
    }

    public void removeCache(String... keys) {
        cacheUtil.removeCache(getCacheName(), keys);
    }
}
