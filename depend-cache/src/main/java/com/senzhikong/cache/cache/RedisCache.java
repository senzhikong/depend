package com.senzhikong.cache.cache;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author shu
 */
@Getter
@Setter
public class RedisCache implements IBaseCache {

    private RedisTemplate<String, Object> redisTemplate;
    private String name;
    private String prefix = "";


    @Override
    @NonNull
    public Object getNativeCache() {
        return this.redisTemplate;
    }

    @Override
    public ValueWrapper get(@NonNull Object key) {
        final String keyStr = prefix + name + "-" + key;
        Object value = redisTemplate.opsForValue().get(keyStr);
        return (value != null ? new SimpleValueWrapper(value) : null);
    }

    @Override
    public void put(@NonNull Object key, Object value) {
        put(key, value, null);
    }

    @Override
    public void evict(@NonNull Object key) {
        final String keyStr = prefix + name + "-" + key;
        redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(keyStr.getBytes()));
    }

    @Override
    public void clear() {
        final String keyStr = prefix + name + "-*";
        final Set<String> keys = redisTemplate.keys(keyStr);
        if (keys == null) {
            return;
        }
        redisTemplate.execute((RedisCallback<String>) connection -> {
            for (String key : keys) {
                connection.del(key.getBytes());
            }
            return "ok";
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(@NonNull Object key, Class<T> type) {
        final String keyStr = prefix + name + "-" + key;
        try {
            return (T) redisTemplate.opsForValue().get(keyStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ValueWrapper putIfAbsent(@NonNull Object key, Object value) {
        put(key, value);
        return new SimpleValueWrapper(value);
    }

    @Override
    public <T> T get(@NonNull Object arg0, @NonNull Callable<T> arg1) {
        try {
            return arg1.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void put(Object key, Object value, Duration duration) {
        final String keyStr = prefix + name + "-" + key;
        if (duration == null) {
            redisTemplate.opsForValue().set(keyStr, value);
        } else {
            redisTemplate.opsForValue().set(keyStr, value, duration);
        }
    }
}
