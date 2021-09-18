package com.senzhikong.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Shu.zhou
 * @date 2019年5月5日下午2:20:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@RequestMapping
@ResponseBody
public @interface MemberApi {
    boolean login();

    @AliasFor("path") String[] value() default {};

    @AliasFor("value") String[] path() default {};

    String log() default "";

    RequestMethod[] method() default {};

    String[] produces() default {MediaType.APPLICATION_JSON_VALUE};
}
