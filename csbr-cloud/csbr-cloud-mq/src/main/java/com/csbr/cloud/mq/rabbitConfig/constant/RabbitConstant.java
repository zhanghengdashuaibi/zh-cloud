package com.csbr.cloud.mq.rabbitConfig.constant;

/**
 * @author zhangheng
 * @date 2019/11/8 13:53
 */
public class RabbitConstant {

    /**
     * 消息编码
     */
    public static final String MESSAGE_ENCODING = "UTF-8";
    public static final String EXCHANGE_ISSUE = "exchange_message_issue";
    public static final String QUEUE_ISSUE_USER = "queue_message_issue_user";
    public static final String QUEUE_ISSUE_ALL_USER = "queue_message_issue_all_user";
    public static final String QUEUE_ISSUE_ALL_DEVICE = "queue_message_issue_all_device";
    public static final String QUEUE_ISSUE_CITY = "queue_message_issue_city";
    public static final String ROUTING_KEY_ISSUE_USER = "routing_key_message_issue_user";
    public static final String ROUTING_KEY_ISSUE_ALL_USER = "routing_key_message_issue_all_user";
    public static final String ROUTING_KEY_ISSUE_ALL_DEVICE = "routing_key_message_issue_all_device";
    public static final String ROUTING_KEY_ISSUE_CITY = "routing_key_message_issue_city";
    public static final String EXCHANGE_PUSH = "exchange_message_push";
    public static final String QUEUE_PUSH_RESULT = "queue_message_push_result";
}
