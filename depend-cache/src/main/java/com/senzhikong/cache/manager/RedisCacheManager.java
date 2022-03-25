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
//    @Resource
//    protected RedisTemplate<String, Object> redisTemplate;

    @Override
    public IBaseCache createCache(String cacheName) {
        RedisCache cache = new RedisCache();
        RedisTemplate<String, Object> redisTemplate;
        if (SpringContextHolder.containsBean(cacheName + "RedisTemplate")) {
            redisTemplate = SpringContextHolder.getBean(cacheName + "RedisTemplate");
        } else if (SpringContextHolder.containsBean("defaultRedisTemplate")) {
            redisTemplate = SpringContextHolder.getBean("defaultRedisTemplate");
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
