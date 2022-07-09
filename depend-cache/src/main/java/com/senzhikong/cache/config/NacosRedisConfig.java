package com.senzhikong.cache.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.senzhikong.cache.manager.RedisCacheManager;
import com.senzhikong.cache.redis.MyRedisTemplate;
import com.senzhikong.spring.SpringContextHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author shu
 */
@Getter
@Setter
@Slf4j
public class NacosRedisConfig extends CachingConfigurerSupport implements InitializingBean {
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddress;
    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String namespace;
    private JSONObject config;


    /**
     * 最小空闲连接数
     */
    @Value("${spring.redis.jedis.pool.minIdle}")
    private int minIdle = 1;
    /**
     * 最大空闲连接数
     */
    @Value("${spring.redis.jedis.pool.maxIdle}")
    private int maxIdle = 5;
    /**
     * 最大连接数
     */
    @Value("${spring.redis.jedis.pool.maxActive}")
    private int maxActive = 10;
    /**
     * 建立连接最长等待时间
     */
    @Value("${spring.redis.jedis.pool.maxWait}")
    private long maxWait = 1000;

    private ConfigService configService;

    /**
     * 连接池配置
     */
    public JedisPoolConfig poolConfig(int minIdle, int maxIdle, int maxTotal, long maxWaitMillis,
            boolean testOnBorrow) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }

    /**
     * 配置工厂
     */
    public RedisConnectionFactory connectionFactory(String host, int port, String password, int minIdle, int maxIdle,
            int maxTotal, long maxWaitMillis, int index) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        if (index != 0) {
            redisStandaloneConfiguration.setDatabase(index);
        }
        if (StringUtils.isNotEmpty(password)) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
        JedisClientConfiguration jedisClientConfiguration = jedisClientConfigurationBuilder
                .usePooling().poolConfig(this.poolConfig(minIdle, maxIdle, maxTotal, maxWaitMillis, false)).build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration);
        jedisConnectionFactory.afterPropertiesSet();
        jedisConnectionFactory.getConnection();
        return jedisConnectionFactory;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddress);
        properties.setProperty("namespace", namespace);
        this.configService = NacosFactory.createConfigService(properties);
        listenCrossConfig("database.redis", "database");
    }

    /**
     * 监听Nacos下发的redis配置
     */
    public void listenCrossConfig(String dataId, String group) {
        try {
            String configInfo = configService.getConfig(dataId, group, 30000L);
            refreshRedisCache(configInfo);
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    refreshRedisCache(configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error("从nacos接收redis配置出错!!!", e);
        }
    }

    public void refreshRedisCache(String configInfo) {
        log.info("nacos更新redis缓存配置：\n" + configInfo);
        this.config = JSONObject.parseObject(configInfo);
        for (String key : this.config.keySet()) {
            JSONObject json = this.config.getJSONObject(key);
            RedisConnectionFactory connectionFactory = connectionFactory(json.getString("host"),
                    json.getInteger("port"),
                    json.getString("password"),
                    minIdle,
                    maxIdle,
                    maxActive,
                    maxWait,
                    json.getInteger("database"));
            if (StringUtils.equals("default", key)) {
                SpringContextHolder.removeBean("redisTemplate");
                SpringContextHolder.registerBean("redisTemplate", MyRedisTemplate.class, connectionFactory);
            }
            SpringContextHolder.removeBean(key + "RedisTemplate");
            SpringContextHolder.registerBean(key + "RedisTemplate", MyRedisTemplate.class, connectionFactory);
            log.info("声明redis缓存：" + key);
            RedisCacheManager redisCacheManager = SpringContextHolder.getBean(RedisCacheManager.class);
            if (redisCacheManager != null) {
                redisCacheManager.initCache();
            }
        }
    }
}
