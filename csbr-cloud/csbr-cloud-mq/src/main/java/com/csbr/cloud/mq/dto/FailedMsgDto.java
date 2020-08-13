package com.csbr.cloud.mq.dto;

import lombok.Data;

/**
 * @program: message
 * @description:
 * @author: Huanglh
 * @create: 2019-11-13 18:19
 **/
@Data
public class FailedMsgDto {
    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 消息队列类型
     */
    private String mqType;

    /**
     * 消费者程序方法全路径(包名+类名+方法名)
     */
    private String functionPackage;

    /**
     * topic值(kafka用)
     */
    private String topic;

    /**
     * partition值(kafka用)
     */
    private Integer partition;

    /**
     * 消息偏移量(index)
     */
    private Integer offset;

    /**
     * 重试次数(只记录主动重试的次数)
     */
    private Integer retryTimes;
}
