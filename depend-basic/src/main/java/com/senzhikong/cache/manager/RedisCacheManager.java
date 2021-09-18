package com.senzhikong.cache.manager;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.cache.cache.RedisCache;
import com.senzhikong.util.string.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author Shu.Zhou
 */
@Getter
@Setter
public class RedisCacheManager extends BaseCacheManager {
    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    @Override
    public IBaseCache createCache(String cacheName) {
        RedisCache cache = new RedisCache();
        cache.setName(cacheName);
        cache.setRedisTemplate(redisTemplate);
        cache.setLiveTime(liveTime);
        cache.setPrefix(StringUtil.isEmpty(prefix) ? "" : prefix + "-");
        cacheMap.put(cacheName, cache);
        log.info("Create Redis Cache:" + cacheName);
        return cache;
    }
}
