package com.senzhikong.cloud;


import com.senzhikong.cache.manager.RedisCacheManager;
import com.senzhikong.db.config.TransactionConfig;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shu
 */
@Configuration
public class AppConfig {

    @Bean
    public CacheManager cacheManager() {
        return new RedisCacheManager();
    }

    @Bean
    public TransactionConfig transactionConfig() {
        return new TransactionConfig("execution(* com.demo..*.service.impl..*.*(..))");
    }
}
