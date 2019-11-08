package com.csbr.cloud.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zhangheng
 * @date 2019/10/25 10:46
 */
@Data
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Component
public class RedisClusterConfigurationProperties {

    /**
     * redis节点ip和端口列表
     * spring.redis.cluster.nodes[0] = 127.0.0.1:7379
     * spring.redis.cluster.nodes[1] = 127.0.0.1:7380
     */
    List<String> nodes;

    Map<String,Long> keyTtl;

    private String password;

    private Integer database;

}
