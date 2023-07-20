package com.senzhikong.cache.manager;

import com.senzhikong.cache.cache.IBaseCache;
import com.senzhikong.module.Module;
import com.senzhikong.spring.SpringContextHolder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import jakarta.annotation.Resource;
import java.util.*;
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
        Map<String, Module> moduleBeanMap = SpringContextHolder.getApplicationContext().getBeansOfType(Module.class);
        for (Module module : moduleBeanMap.values()) {
            cacheNames.addAll(Arrays.asList(module.getCacheNames()));
        }
        initCache();
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return cacheMap.values();
    }


    public void initCache() {
        for (String cacheName : cacheNames) {
            getOrCreateCache(cacheName);
        }
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
    public Collection<String> getCacheNames() {
        return cacheNames;
    }


}
