package com.csbr.cloud.es.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangheng
 * @date 2020/7/16 15:54
 */
@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticsearchRestClientProperties {
    /**
     * 这里可以配置多个地址，在配置文件里面可以有：spring.elasticsearch.rest.uris=http://192.168.164.135:9200
     */
    private List<String> uris = new ArrayList<>(Collections.singletonList("http://139.9.190.186:9200"));

    /**
     * 连接用户名.
     */
    private String username;

    /**
     * 连接密码.
     */
    private String password;


}
