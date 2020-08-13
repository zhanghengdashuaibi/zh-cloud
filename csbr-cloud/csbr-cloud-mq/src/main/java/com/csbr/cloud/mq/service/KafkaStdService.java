package com.csbr.cloud.mq.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @program: csbr-cloud-mq
 * @description:
 * @author: Huanglh
 * @create: 2019-11-14 10:34
 **/
@Slf4j
public class KafkaStdService {
    private KafkaTemplate kafkaTemplate;

    public KafkaStdService(KafkaTemplate template) {
        this.kafkaTemplate = template;
    }

    public void stdSendMsg(String topic, String value) {
        this.stdSendMsg(topic, null, value);
    }

    public void stdSendMsg(String topic, String key, String value) {
        ListenableFuture<SendResult> future = kafkaTemplate.send(topic, key, value);
        log.info("发送消息，key：" + key + " value：" + value);
        future.addCallback(new ListenableFutureCallback<SendResult>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("消息发送失败，key：" + key + " value：" + value + " error：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送成功，key：" + key + " value：" + value);
            }
        });
    }

    /**
     * 使用record发送消息
     * 可以自行指定partition，并可在headers中添加特殊内容
     * 添加headers的例子：
     * RecordHeaders headers = new RecordHeaders();
     * headers.add(new RecordHeader("key", "value".getBytes()));
     *
     * @param record
     */
    public void stdSendMsg(ProducerRecord record) {
        ListenableFuture<SendResult> future = kafkaTemplate.send(record);

        log.info("发送消息(record)，topic：" + record.topic() + " partition：" + record.partition() +
                " key：" + record.key() + " value：" + record.value() + " headers：" + JSON.toJSONString(record.headers()));
        future.addCallback(new ListenableFutureCallback<SendResult>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("消息发送失败(record)，topic：" + record.topic() + " partition：" + record.partition() +
                        " key：" + record.key() + " value：" + record.value() + " error：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送成功(record)，topic：" + record.topic() + " partition：" + record.partition() +
                        " key：" + record.key() + " value：" + record.value());
            }
        });
    }
}
