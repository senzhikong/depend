package com.senzhikong.cache.cache;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * @author shu
 */
public interface IBaseCache extends Cache {
    /**
     * 添加缓存
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param duration 超时时间
     */
    void put(Object key, @Nullable Object value, Duration duration);
}
