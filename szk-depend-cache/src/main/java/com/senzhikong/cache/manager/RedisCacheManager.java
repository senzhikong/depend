package com.senzhikong.cache.manager;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.cache.cache.RedisCache;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Shu.Zhou
 */
@Getter
@Setter
public class RedisCacheManager extends BaseCacheManager {
    private static final String DEFAULT_REDIS_TEMPLATE = "defaultRedisTemplate";
    private static final String REDIS_TEMPLATE = "RedisTemplate";
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public IBaseCache createCache(String cacheName) {
        RedisCache cache = new RedisCache();
        cache.setName(cacheName);
        cache.setRedisTemplate(redisTemplate);
        cache.setPrefix(StringUtils.isBlank(prefix) ? "" : prefix + "-");
        cacheMap.put(cacheName, cache);
        log.info("Create Redis Cache:" + cacheName);
        return cache;
    }
}
