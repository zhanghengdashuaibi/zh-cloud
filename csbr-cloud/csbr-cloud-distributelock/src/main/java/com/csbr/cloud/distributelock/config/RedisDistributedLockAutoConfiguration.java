package com.csbr.cloud.distributelock.config;

import com.csbr.cloud.distributelock.lock.DistributedLock;
import com.csbr.cloud.distributelock.lock.RedisDistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author zhangheng
 * @date 2019/7/17 10:29
 * 装配分布式锁的bean
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)//自动注入在RedisAutoConfiguration类加载后
public class RedisDistributedLockAutoConfiguration {

    @Bean
    public com.csbr.cloud.distributelock.config.RedisDistributedLockAspect redisDistributedLockAspect(){
        return new com.csbr.cloud.distributelock.config.RedisDistributedLockAspect();
    }
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(){
        return new RedisTemplate<Object, Object>();
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)//只有RedisTemplate类存在才会去创建RedisDistributedLock
    public DistributedLock redisDistributedLock(RedisTemplate<Object, Object> redisTemplate){
        return new RedisDistributedLock(redisTemplate);
    }

}
