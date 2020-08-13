package com.csbr.cloud.mq.kafkaConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangheng
 * @date 2019/11/8 11:20
 * kafka消费者属性
 */
@Data
@Component
@ConfigurationProperties("csbr.kafka.consumer")
public class KafkaConsumerProperties {


    /**
     * 链接地址
     */
    private String bootstrapServers;

    /**
     * 是否启用自动提交 offset
     */
    private Boolean enableAutoCommit=false;

    /**
     * 自动提交间隔时间(MS)，仅在自动提交开启时有效
     */
    private Integer autoCommitInterval=100;

    /**
     *最大 poll 消息数量
     */
    private Integer maxPollRecords=500;

    /**
     *自动重置 offset。 latest：从最后开始，earliest: 从 offset 为 0 开始
     */
    private String autoOffsetReset="latest";

    /**
     *设置消费的线程数
     */
    private Integer concurrency=3;

    /**
     * 消费组id
     */
//    private String groupId;

    /**
     *只限自动提交
     */
    private Long pollTimeout=3000L;

    /**
     *session 超时时间
     */
    private String sessionTimeout;

    /**
     * 是否开启批量消费，true表示批量消费
     */
    private Boolean batchListener=true;

    /**
     *最大 poll 消息间隔时间，如果处理时间过长会导致异常
     */
    private Integer maxPollInterval=10000;

    /**
     *设置拉取数据的大小,15M  15728640
     */
    private Integer maxPartitionFetchBytes=15728640;

    /**
     * 消息 key 的反序列化方法
     */
    private String keydeserializer;

    /**
     * 消息 value 的反序列化方法
     */
    private String valueDeserializer;


}
