package com.senzhikong.cache.cache;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.time.Duration;

public interface IBaseCache extends Cache {
    void put(Object var1, @Nullable Object var2, Duration duration);
}
