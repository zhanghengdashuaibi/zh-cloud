package com.csbr.cloud.mq.rabbitConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangheng
 * @date 2019/11/8 13:41
 * rabbitmq自定义配置
 */
@Data
@Component
@ConfigurationProperties("csbr.rabbitmq")
public class RabbitProperties {

    /**
     * ip
     */
//    private String host;

    /**
     * 端口
     */
//    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * ip+端口
     */
    private String addresses;

    private String virtualHost;

}
