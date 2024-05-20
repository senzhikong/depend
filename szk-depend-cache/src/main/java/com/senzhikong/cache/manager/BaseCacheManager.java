package com.senzhikong.cache.manager;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.spring.SpringContextHolder;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Shu.Zhou
 */
@Getter
@Setter
public abstract class BaseCacheManager extends AbstractCacheManager {
    protected Log log = LogFactory.getLog(getClass());
    protected ConcurrentMap<String, IBaseCache> cacheMap = new ConcurrentHashMap<>(16);

    protected Set<String> cacheNames;
    protected String prefix = "";
    protected int hitCount;
    @Resource
    SpringContextHolder springContextHolder;

    @Override
    public void afterPropertiesSet() {
        if (cacheNames == null) {
            cacheNames = new HashSet<>();
        }
        super.afterPropertiesSet();
    }

    @Override
    @NonNull
    protected Collection<? extends Cache> loadCaches() {
        return cacheMap.values();
    }

    /**
     * 创建缓存
     *
     * @param cacheName 缓存名称
     * @return 创建好的缓存
     */
    public abstract IBaseCache createCache(String cacheName);

    public IBaseCache getOrCreateCache(String cacheName) {
        IBaseCache cache = cacheMap.get(cacheName);
        return cache == null ? createCache(cacheName) : cache;
    }


    @Override
    @NonNull
    public Collection<String> getCacheNames() {
        return cacheNames;
    }

}
