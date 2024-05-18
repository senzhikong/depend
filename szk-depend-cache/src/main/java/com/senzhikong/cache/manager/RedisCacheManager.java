package com.senzhikong.cache.manager;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.cache.cache.RedisCache;
import com.senzhikong.spring.SpringContextHolder;
import com.senzhikong.util.string.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Shu.Zhou
 */
@Getter
@Setter
public class RedisCacheManager extends BaseCacheManager {
    private static final String DEFAULT_REDIS_TEMPLATE = "defaultRedisTemplate";
    private static final String REDIS_TEMPLATE = "RedisTemplate";

    @Override
    public IBaseCache createCache(String cacheName) {
        RedisCache cache = new RedisCache();
        RedisTemplate<String, Object> redisTemplate;
        if (SpringContextHolder.containsBean(cacheName + REDIS_TEMPLATE)) {
            redisTemplate = SpringContextHolder.getBean(cacheName + REDIS_TEMPLATE);
        } else if (SpringContextHolder.containsBean(DEFAULT_REDIS_TEMPLATE)) {
            redisTemplate = SpringContextHolder.getBean(DEFAULT_REDIS_TEMPLATE);
        } else {
            redisTemplate = SpringContextHolder.getBean("redisTemplate");
        }
        cache.setName(cacheName);
        cache.setRedisTemplate(redisTemplate);
        cache.setPrefix(StringUtil.isEmpty(prefix) ? "" : prefix + "-");
        cacheMap.put(cacheName, cache);
        log.info("Create Redis Cache:" + cacheName);
        return cache;
    }
}
