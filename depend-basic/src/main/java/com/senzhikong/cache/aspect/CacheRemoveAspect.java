package com.senzhikong.cache.aspect;

import com.senzhikong.cache.CacheUtil;
import com.senzhikong.cache.annotation.CacheRemove;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author Shu.zhou
 * @date 2019年2月1日上午9:41:34
 */
@Slf4j
@Aspect
public class CacheRemoveAspect implements InitializingBean {
    @Resource
    CacheUtil cacheUtil;

    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.senzhikong.cache.annotation.CacheRemove))")
    private void pointcut() {
    }

    @AfterReturning(value = "pointcut()")
    private void process(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);
        if (cacheRemove == null) {
            return;
        }
        String value = cacheRemove.value();
        String[] keys = cacheRemove.key(); // 需要移除的正则key
        cacheUtil.removeCache(value, keys);
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("临时目录：：" + System.getProperty("java.io.tmpdir"));
        log.debug("初始化完成缓存移除组件");
    }
}
