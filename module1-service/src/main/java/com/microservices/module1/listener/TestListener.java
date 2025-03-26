package com.microservices.module1.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author nsh
 * @data 2025/3/26 14:34
 * @description
 **/
@Slf4j
@Component
public class TestListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "test.queue"),
            exchange = @Exchange(name = "test.exchange",type = ExchangeTypes.DIRECT),
            key = "5mm"
    ))
    public void listenerTest(String text){
        log.info("消费者收到消息:{}",text);
    }
}
