package com.csbr.cloud.mq.kafkaConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangheng
 * @date 2019/11/8 10:59
 * kafka生产者自定义配置属性
 */
@Data
@Component
@ConfigurationProperties("csbr.kafka.producer")
public class KafkaProducerProperties {

    /**
     * 链接地址 有多个则逗号拼接
     */
    private String bootstrapServers;

    /**
     * 发送失败后的重复发送次数
     */
    private Integer retries;

    /**
     * 一次最多发送数据量
     */
    private Integer batchSize;

    /**
     * 32M批处理缓冲区
     */
    private Integer bufferMemory;


    private Integer linger;
}
