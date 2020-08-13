package com.csbr.cloud.mq.kafkaConfig;

import com.csbr.cloud.mq.service.KafkaStdService;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangheng
 * @date 2019/11/8 10:57
 * kafka生产者配置
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties
//如果该值为空，则返回false;如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。如果返回值为false，则该configuration不生效；为true则生效
@ConditionalOnProperty(name = "csbr.kafka.producer.enable", havingValue = "true")
public class KafkaProducerConfig {

    /**
     * 读取kafka生产者配置文件中的属性
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaProducerProperties kafkaProducerProperties() {
        return new KafkaProducerProperties();
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(7);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties().getBootstrapServers());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerProperties().getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProperties().getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties().getLinger());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerProperties().getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    private ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        /*producerFactory.transactionCapable();
        producerFactory.setTransactionIdPrefix("hous-");*/
        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 标准kafka服务注册到容器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean({KafkaStdService.class})
    public KafkaStdService kafkaStdService() {
        return new KafkaStdService(kafkaTemplate());
    }

}
