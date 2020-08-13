package com.csbr.cloud.mq.kafkaConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangheng
 * @date 2019/11/8 11:13
 * kafka消费者配置
 */
@Slf4j
@Configuration
@EnableKafka
@EnableConfigurationProperties
//如果该值为空，则返回false;如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。如果返回值为false，则该configuration不生效；为true则生效
@ConditionalOnProperty(name="csbr.kafka.consumer.enable", havingValue="true")
public class KafkaConsumerConfig {

    /**
     * 读取kafka消费者属性
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaConsumerProperties kafkaConsumerProperties(){
        return new KafkaConsumerProperties();
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(11);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerProperties().getAutoCommitInterval());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties().getBootstrapServers());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerProperties().getEnableAutoCommit());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerProperties().getMaxPollRecords());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties().getAutoOffsetReset());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerProperties().getSessionTimeout());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerProperties().getMaxPollInterval());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafkaConsumerProperties().getMaxPartitionFetchBytes());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties().getGroupId());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        try {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Class.forName(kafkaConsumerProperties().getKeydeserializer()));
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Class.forName(kafkaConsumerProperties().getValueDeserializer()));
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return props;
    }


    /**
     * 并发数6
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean(name = "kafkaBatchListener6")
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaBatchListener6() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaListenerContainerFactory();
//        factory.setConcurrency(kafkaConsumerProperties().getConcurrency6());
//        return factory;
//    }

    /**
     * 并发数3
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean(name = "kafkaBatchListener3")
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaBatchListener3() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaListenerContainerFactory();
//        factory.setConcurrency(kafkaConsumerProperties().getConcurrency3());
//        return factory;
//    }

//    private ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        //批量消费
//        factory.setBatchListener(kafkaConsumerProperties().getBatchListener());
//        //如果消息队列中没有消息，等待timeout毫秒后，调用poll()方法。
//        // 如果队列中有消息，立即消费消息，每次消费的消息的多少可以通过max.poll.records配置。
//        //手动提交无需配置
//        factory.getContainerProperties().setPollTimeout(kafkaConsumerProperties().getPollTimeout());
//        // 设置提交偏移量的方式， MANUAL_IMMEDIATE 表示消费一条提交一次；MANUAL表示批量提交一次
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        //批量消费
        factory.setBatchListener(kafkaConsumerProperties().getBatchListener());
        factory.setConcurrency(kafkaConsumerProperties().getConcurrency());
//        factory.setBatchListener(true);
        //如果消息队列中没有消息，等待timeout毫秒后，调用poll()方法。
        // 如果队列中有消息，立即消费消息，每次消费的消息的多少可以通过max.poll.records配置。
        //手动提交无需配置
        factory.getContainerProperties().setPollTimeout(kafkaConsumerProperties().getPollTimeout());
        // 手动 commit offset 必须要配置提交方式
        // 设置提交偏移量的方式， MANUAL_IMMEDIATE 表示消费一条提交一次；MANUAL表示批量提交一次
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    private ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * 消费异常处理器
     *
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, e, consumer) -> {
            log.error("Kafka消费异常：" + message.getPayload().toString());
            // 批量处理异常
//            MessageHeaders headers = message.getHeaders();
//            List<String> topics = headers.get(KafkaHeaders.RECEIVED_TOPIC, List.class);
//            List<Integer> partitions = headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, List.class);
//            List<Long> offsets = headers.get(KafkaHeaders.OFFSET, List.class);
//            Map<TopicPartition, Long> offsetsToReset = new HashMap<>();
            return null;
        };
    }

}
