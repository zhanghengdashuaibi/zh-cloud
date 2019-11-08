package com.csbr.cloud.mq.rabbitConfig;

import com.csbr.cloud.mq.rabbitConfig.constant.RabbitConstant;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangheng
 * @date 2019/11/8 13:38
 * RabbitMq自定义配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
//如果该值为空，则返回false;如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。如果返回值为false，则该configuration不生效；为true则生效
@ConditionalOnProperty(name="csbr.rabbitMq.enable", havingValue="true")
public class RabbitMqConfig {

    @Bean
    @ConditionalOnMissingBean
    public RabbitProperties rabbitProperties(){
        return new RabbitProperties();
    }



    @Bean
    public Queue issueUserQueue() {
        return new Queue(RabbitConstant.QUEUE_ISSUE_USER);
    }

    @Bean
    public Queue issueAllUserQueue() {
        return new Queue(RabbitConstant.QUEUE_ISSUE_ALL_USER);
    }

    @Bean
    public Queue issueAllDeviceQueue() {
        return new Queue(RabbitConstant.QUEUE_ISSUE_ALL_DEVICE);
    }

    @Bean
    public Queue issueCityQueue() {
        return new Queue(RabbitConstant.QUEUE_ISSUE_CITY);
    }

    @Bean
    public Queue pushResultQueue() {
        return new Queue(RabbitConstant.QUEUE_PUSH_RESULT);
    }

    @Bean
    public DirectExchange issueExchange() {
        return new DirectExchange(RabbitConstant.EXCHANGE_ISSUE);
    }

    @Bean
    public DirectExchange pushExchange() {
        // 参数1：队列
        // 参数2：是否持久化
        // 参数3：是否自动删除
        return new DirectExchange(RabbitConstant.EXCHANGE_PUSH, true, true);
    }

    @Bean
    public Binding issueUserQueueBinding(@Qualifier("issueUserQueue") Queue queue,
                                         @Qualifier("issueExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.ROUTING_KEY_ISSUE_USER);
    }

    @Bean
    public Binding issueAllUserQueueBinding(@Qualifier("issueAllUserQueue") Queue queue,
                                            @Qualifier("issueExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.ROUTING_KEY_ISSUE_ALL_USER);
    }

    @Bean
    public Binding issueAllDeviceQueueBinding(@Qualifier("issueAllDeviceQueue") Queue queue,
                                              @Qualifier("issueExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.ROUTING_KEY_ISSUE_ALL_DEVICE);
    }

    @Bean
    public Binding issueCityQueueBinding(@Qualifier("issueCityQueue") Queue queue,
                                         @Qualifier("issueExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.ROUTING_KEY_ISSUE_CITY);
    }

    @Bean
    public Binding pushResultQueueBinding(@Qualifier("pushResultQueue") Queue queue,
                                          @Qualifier("pushExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).withQueueName();
    }

    @Bean
    public CachingConnectionFactory defaultConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost(rabbitProperties().getHost());
//        connectionFactory.setPort(rabbitProperties().getPort());
        connectionFactory.setUsername(rabbitProperties().getUsername());
        connectionFactory.setPassword(rabbitProperties().getPassword());
        connectionFactory.setAddresses(rabbitProperties().getAddresses());
        connectionFactory.setVirtualHost(rabbitProperties().getVirtualHost());
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            @Qualifier("defaultConnectionFactory") CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public AmqpTemplate rabbitTemplate(@Qualifier("defaultConnectionFactory") CachingConnectionFactory connectionFactory)
    {
        return new RabbitTemplate(connectionFactory);
    }

}
