package com.senzhikong.web.aop;

import com.alibaba.fastjson.JSON;
import com.senzhikong.exception.AuthException;
import com.senzhikong.exception.DataException;
import com.senzhikong.util.DateUtils;
import com.senzhikong.util.string.StringUtil;
import com.senzhikong.web.annotation.ApiController;
import com.senzhikong.web.annotation.ApiMethod;
import com.senzhikong.web.annotation.PartnerApi;
import com.senzhikong.web.util.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * @author shu
 */
@Slf4j
public class BasicAspect {
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
    @Resource
    protected HttpSession session;

    protected String getClassName(ProceedingJoinPoint pjp) {
        String controllerName = null;
        Api api = pjp.getTarget().getClass().getAnnotation(Api.class);
        if (api != null && api.tags() != null && api.tags().length > 0) {
            controllerName = Arrays.toString(api.tags());
        }
        if (StringUtil.isEmpty(controllerName)) {
            ApiController apiController = pjp.getTarget().getClass().getAnnotation(ApiController.class);
            if (apiController != null) {
                controllerName = apiController.log();
            }
        }
        return controllerName;
    }

    protected String getMethodName(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String methodName = null;
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null && apiOperation.value() != null) {
            methodName = apiOperation.value();
        }
        if (StringUtil.isEmpty(methodName)) {
            ApiMethod apiMethod = pjp.getTarget().getClass().getAnnotation(ApiMethod.class);
            if (apiMethod != null) {
                methodName = apiMethod.log();
            }
        }
        if (StringUtil.isEmpty(methodName)) {
            PartnerApi partnerApi = pjp.getTarget().getClass().getAnnotation(PartnerApi.class);
            if (partnerApi != null) {
                methodName = partnerApi.log();
            }
        }
        return methodName;
    }

    protected StringBuilder requestName(ProceedingJoinPoint pjp) {
        StringBuilder apiName = new StringBuilder();
        try {
            String controllerName = getClassName(pjp);
            apiName.append(controllerName);
            apiName.append(" - ");
            String methodName = getMethodName(pjp);
            apiName.append(methodName);
        } catch (Exception ignored) {
        }
        return apiName;
    }

    protected void visitLog(ProceedingJoinPoint pjp, Object res) {
        StringBuilder apiName = requestName(pjp);
        String ip = IpUtil.getRemoteHost(request);
        String targetName = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        final StringBuilder normalMsg = new StringBuilder();
        normalMsg.append("\n---------------------------------- RequestStart ----------------------------------");
        normalMsg.append("\nRequest Name:\t").append(apiName);
        normalMsg.append("\nRequest Method:\t").append("class=").append(targetName).append(",method=")
                 .append(methodName);
        normalMsg.append("\nRequest Time:\t").append(DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        normalMsg.append("\nRequest Params:\t").append(JSON.toJSONString(pjp.getArgs()));
        normalMsg.append("\nRequest Host:\t").append(ip).append("  ").append(request.getMethod());
        normalMsg.append("\nResponse Result:\t");
        if (res != null) {
            normalMsg.append(res);
        }
        normalMsg.append("\n---------------------------------- RequestEnd ----------------------------------\n");
        log.info(normalMsg.toString());
    }

    protected void feignLog(ProceedingJoinPoint pjp, Object res) {
        StringBuilder apiName = requestName(pjp);
        String targetName = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        final StringBuilder normalMsg = new StringBuilder();
        normalMsg.append("\n---------------------------------- FeignStart ----------------------------------");
        normalMsg.append("\nFeign Name:\t").append(apiName);
        normalMsg.append("\nFeign Method:\t").append("class=").append(targetName).append(",method=")
                 .append(methodName);
        normalMsg.append("\nFeign Time:\t").append(DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        normalMsg.append("\nFeign Params:\t");
        normalMsg.append(JSON.toJSONString(pjp.getArgs()));
        normalMsg.append("\nFeign Result:\t");
        if (res != null) {
            normalMsg.append(res);
        }
        normalMsg.append("\n---------------------------------- FeignEnd ----------------------------------\n");
        log.info(normalMsg.toString());
    }

    protected void errorLog(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        sb.append("抛出异常");
        sb.append(t.getClass()).append("[").append(t.getMessage()).append("]");
        sb.append("\r\n");
        StackTraceElement[] stackTraceElements = t.getStackTrace();
        if (t instanceof AuthException || t instanceof DataException) {
            log.error(sb.toString());
            return;
        }
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            String stackTrace = String
                    .format("at %s.%s(%s:%s)", stackTraceElement.getFileName(), stackTraceElement.getMethodName(),
                            stackTraceElement.getFileName(), stackTraceElement.getLineNumber());
            sb.append(stackTrace);
            sb.append("\r\n");
        }
        log.error(sb.toString());
    }
}
