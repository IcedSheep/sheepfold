package com.sheep.sheepfold.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {
 
    private String host;
 
    private int port;
 
    private int database;
 
    private String password;
 
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s",host,port);
        // 单节点Redis
        config.useSingleServer().setAddress(redisAddress).setDatabase(database).setPassword(password);
        return Redisson.create(config);
    }
 
}