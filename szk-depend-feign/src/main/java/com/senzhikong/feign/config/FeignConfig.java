package com.senzhikong.feign.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author shu.zhou
 */
@Configuration
public class FeignConfig {

    @Resource
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    @Bean
    public Decoder decoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        // ObjectMapper为了保障线程安全性，里面的配置类都是一个不可变的对象
        ObjectMapper mapper = jackson2ObjectMapperBuilder.build();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(mapper);
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(mappingJackson2HttpMessageConverter);
        return () -> httpMessageConverters;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().toList());
    }
}

