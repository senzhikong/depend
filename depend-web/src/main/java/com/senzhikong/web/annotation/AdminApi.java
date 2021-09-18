package com.senzhikong.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ResponseBody
@RequestMapping
@RestController
public @interface AdminApi {

    @AliasFor("path") String[] value() default {};

    @AliasFor("value") String[] path() default {};

    String name() default "";

    String description() default "";

    RequestMethod[] method() default {RequestMethod.POST};

    @AliasFor(annotation = RequestMapping.class) String[] produces() default {MediaType.APPLICATION_JSON_VALUE};
}
