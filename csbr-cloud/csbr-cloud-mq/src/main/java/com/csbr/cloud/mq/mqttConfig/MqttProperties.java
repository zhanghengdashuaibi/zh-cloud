package com.csbr.cloud.mq.mqttConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangheng
 * @date 2019/11/8 15:07
 */
@Data
@Component
@ConfigurationProperties("csbr.mqtt")
public class MqttProperties {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 推送信息的连接地址，如果有多个，用逗号隔开，如：tcp://127.0.0.1:61613,tcp://192.168.1.61:61613
     */
    private String url;
    /**
     * 生产者
     * 连接服务器默认客户端ID
     */
    private String producerClientId;
    /**
     * 生产者
     * 默认的推送主题，实际可在调用接口时指定
     */
    private String producerDefaultTopic;
    /**
     * 消费者
     * 连接服务器默认客户端ID
     */
    private String consumerClientId;
    /**
     * 消费者
     * 默认的接收主题，可以订阅多个Topic，逗号分隔
     */
    private String consumerDefaultTopic;
}
