package com.senzhikong.cache.aspect;

import com.senzhikong.cache.CacheUtil;
import com.senzhikong.cache.SimpleKeyGenerator;
import com.senzhikong.cache.annotation.CacheRemove;
import com.senzhikong.cache.annotation.Cacheable;
import com.senzhikong.spring.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author Shu.zhou
 * @date 2019年2月1日上午9:41:34
 */
@Slf4j
@Aspect
@Component
public class CacheAspect implements InitializingBean {
    @Resource
    private CacheUtil cacheUtil;
    @Resource
    private SimpleKeyGenerator simpleKeyGenerator;

    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.senzhikong.cache.annotation.CacheRemove))")
    private void removePointcut() {
    }


    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.senzhikong.cache.annotation.Cacheable))")
    private void aroundPointcut() {
    }

    private KeyGenerator getKeyGenerator(String keyGenerator) {
        if (StringUtils.isBlank(keyGenerator)) {
            return simpleKeyGenerator;
        }
        return SpringContextHolder.getBean(keyGenerator);
    }

    private Duration string2Duration(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String unit = str.substring(str.length() - 1).toLowerCase();
        long num = Long.parseLong(str.substring(0, str.length() - 1));
        switch (unit) {
            case "s":
                return Duration.ofSeconds(num);
            case "m":
                return Duration.ofMinutes(num);
            case "h":
                return Duration.ofHours(num);
            case "d":
                return Duration.ofDays(num);
            default:
                return null;
        }
    }

    @Around("aroundPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object target = pjp.getTarget();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Object[] params = pjp.getArgs();
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        String[] cacheNames = cacheable.cacheNames();
        Duration duration = string2Duration(cacheable.duration());
        String cacheKey = cacheable.key();
        if (StringUtils.isBlank(cacheKey)) {
            KeyGenerator keyGenerator = getKeyGenerator(cacheable.keyGenerator());
            cacheKey = keyGenerator.generate(target, method, params).toString();
        } else {
            cacheKey += SimpleKeyGenerator.paramKey(params);
        }
        Object res;
        for (String cacheName : cacheNames) {
            res = cacheUtil.findCache(cacheName, cacheKey);
            if (res != null) {
                return res;
            }
        }
        res = pjp.proceed();
        if (res != null) {
            for (String cacheName : cacheNames) {
                cacheUtil.saveCache(cacheName, cacheKey, res, duration);
            }
        }
        return res;
    }

    @AfterReturning(value = "removePointcut()")
    private void process(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);
        if (cacheRemove == null) {
            return;
        }
        String value = cacheRemove.value();
        // 需要移除的正则key
        String[] keys = cacheRemove.key();
        cacheUtil.removeCache(value, keys);
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("初始化完成缓存移除组件");
    }
}
