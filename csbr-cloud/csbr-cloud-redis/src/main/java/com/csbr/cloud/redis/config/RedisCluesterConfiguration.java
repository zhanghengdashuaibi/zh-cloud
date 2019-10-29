package com.csbr.cloud.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangheng
 * @date 2019/10/25 10:44
 */
@Configuration
public class RedisCluesterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisClusterConfigurationProperties clusterConfigurationProperties() {
        return new RedisClusterConfigurationProperties();
    }

    @Bean
    public RedisConnectionFactory connectionFactory(RedisClusterConfigurationProperties clusterConfigurationProperties) {
        return new LettuceConnectionFactory(
                new RedisClusterConfiguration(clusterConfigurationProperties.getNodes())
        );
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(getJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(jacksonRedisSerializerPair());
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisCacheConfiguration redisCacheConfiguration, RedisClusterConfigurationProperties clusterConfigurationProperties) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration);

        if(generateRedisTtlConfigurationMap(clusterConfigurationProperties) != null){
            builder.withInitialCacheConfigurations(generateRedisTtlConfigurationMap(clusterConfigurationProperties));

        }
        return builder.build();
    }

    private RedisSerializationContext.SerializationPair jacksonRedisSerializerPair() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        return RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer);
    }

    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    private Map<String,RedisCacheConfiguration> generateRedisTtlConfigurationMap(RedisClusterConfigurationProperties clusterConfigurationProperties) {
        if(clusterConfigurationProperties.getKeyTtl() != null){
            return clusterConfigurationProperties.getKeyTtl()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(o -> o.getKey(), o -> redisCacheConfiguration().entryTtl(Duration.ofSeconds(o.getValue()))));
        }
        return null;
    }

}
