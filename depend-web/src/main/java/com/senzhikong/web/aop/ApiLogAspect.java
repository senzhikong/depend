package com.senzhikong.web.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(10)
public class ApiLogAspect extends BasicAspect {

    @Pointcut("@annotation(com.senzhikong.web.annotation.ApiMethod)")
    public void aspect() {
    }

    /**
     * 管理日志
     *
     * @param pjp 切入点
     * @return 返回结果
     * @throws Throwable 抛出异常
     */
    @Around("aspect()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object res = pjp.proceed();
            visitLog(pjp, res);
            return res;
        } catch (Throwable t) {
            errorLog(t);
            throw t;
        }
    }

}
