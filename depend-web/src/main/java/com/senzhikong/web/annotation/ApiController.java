package com.senzhikong.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@RequestMapping
@RestController
public @interface ApiController {

    @AliasFor("path") String[] value() default {};

    @AliasFor("value") String[] path() default {};

    @AliasFor("name") String log() default "";

    @AliasFor("log") String name() default "";
}
