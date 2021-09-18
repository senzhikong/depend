package com.senzhikong.web.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Shu.zhou
 */
@Aspect
@Component
@Order(101)
public class OpenAspect extends BasicAspect {

    @Pointcut("@annotation(com.senzhikong.web.annotation.OpenApi)")
    public void aspect() {
    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object res = pjp.proceed();
            visitLog(pjp,   res);
            return res;
        } catch (Throwable t) {
            errorLog(t);
            throw t;
        }
    }
}
