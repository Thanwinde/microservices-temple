package com.microservices.module1.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nsh
 * @data 2025/3/26 19:12
 * @description
 **/

@Configuration
//设定RabbitMQ对于投递失败且重复次数耗尽的行为

@ConditionalOnProperty(name = "spring.rabbitmq.listener.simple.retry.enabled", havingValue = "true")
//只有在配置文件允许消费者重试的情况下才会尝试重试

public class ErrorMessageConfig {

    @Bean
    public DirectExchange errorMessageExchange(){
        return new DirectExchange("test.deadMsgExchange");
    }
    //指定死信交换机

    @Bean
    public Queue errorQueue(){
        return new Queue("test.deadMsgQueue",true);
    }
    //指定死信队列
    @Bean
    public Binding errorBinding(){
        return BindingBuilder.bind(errorQueue()).to(errorMessageExchange()).with("error");
    }
    //绑定死信队列和交换机
    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate, "test.deadMsgExchange", "error");
    }
    //设定死信机制
    //RejectAndDontRequeueRecoverer：重试耗尽后，直接reject，丢弃消息。默认就是这种方式
    //ImmediateRequeueMessageRecoverer：重试耗尽后，返回nack，消息重新入队
    //RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机
}
