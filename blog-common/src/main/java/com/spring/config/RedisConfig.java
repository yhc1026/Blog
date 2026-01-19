package com.spring.config;


import com.spring.utils.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
    public RedisUtil redis(StringRedisTemplate stringRedisTemplate){
        return new RedisUtil(stringRedisTemplate);
    }
}
