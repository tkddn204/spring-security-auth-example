package com.rightpair.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import redis.embedded.RedisServer;

@Slf4j
@EnableRedisIndexedHttpSession
@Configuration
class RedisConfig {
    private final RedisServer redisServer = new RedisServer();

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    @PostConstruct
    private void startRedis() {
        try {
            redisServer.start();
        } catch (Exception e) {
            log.error("이미 레디스 서버가 존재합니다. 기존 레디스 서버로 대체합니다.");
        }
    }

    @PreDestroy
    private void stopRedis() {
        try {
            redisServer.stop();
        } catch (Exception ignored) {
        }
    }
}