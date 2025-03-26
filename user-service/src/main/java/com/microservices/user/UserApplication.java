package com.microservices.user;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients(basePackages = "com.microservices.api",defaultConfiguration = com.microservices.api.config.OpenFeignConfig.class)
//对openFeign就行配置，basePackages指对应client类，defaultConfiguration指对当前client的配置
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    //设置消息队列的序列化器
    public MessageConverter messageConverter() {
        //使用jackson作为序列化器
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        //在每次发送消息时都会生成消息id便于跟踪
        messageConverter.setCreateMessageIds(true);
        return messageConverter;
    }
}
