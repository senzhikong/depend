package com.senzhikong.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shu
 */
public class MyRedisTemplate extends RedisTemplate<String, Object> {
    public MyRedisTemplate(RedisConnectionFactory connectionFactory) {
        this.setConnectionFactory(connectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
                java.lang.Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        this.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        this.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        this.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        this.setHashValueSerializer(jackson2JsonRedisSerializer);
        this.afterPropertiesSet();
    }
}
