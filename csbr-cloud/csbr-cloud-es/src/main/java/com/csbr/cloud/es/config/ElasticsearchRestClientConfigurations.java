package com.csbr.cloud.es.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangheng
 * @date 2020/7/16 16:18
 * es配置启动类
 */
@Configuration
public class ElasticsearchRestClientConfigurations {


    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchRestClientProperties properties() {
        return new ElasticsearchRestClientProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClientBuilder elasticsearchRestClientBuilder(ElasticsearchRestClientProperties properties,
                                                            ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
        HttpHost[] hosts = properties.getUris().stream().map(HttpHost::create).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(hosts);
        PropertyMapper map = PropertyMapper.get();
        map.from(properties::getUsername).whenHasText().to((username) -> {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
                    properties.getPassword());
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            builder.setHttpClientConfigCallback(
                    (httpClientBuilder) -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        });
        builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }


}
