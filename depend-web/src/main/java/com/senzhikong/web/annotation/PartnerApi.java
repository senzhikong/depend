package com.senzhikong.web.annotation;

import com.senzhikong.web.util.WebConstants;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ResponseBody
@RequestMapping
@CrossOrigin
public @interface PartnerApi {

    @AliasFor("path") String[] value() default {};

    @AliasFor("value") String[] path() default {};

    String log() default "";

    RequestMethod[] method() default {};

    String[] origins() default {"*"};

    String[] allowedHeaders() default {"Content-Type", "Accept", "Cookie", "set-cookie", WebConstants.WEB_AUTH_TOKEN};

    String allowCredentials() default "true";

    boolean cors() default true;

    String[] produces() default {MediaType.APPLICATION_JSON_VALUE};
}
