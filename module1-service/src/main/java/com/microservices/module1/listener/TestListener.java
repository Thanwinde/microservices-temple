package com.microservices.module1.listener;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author nsh
 * @data 2025/3/26 14:34
 * @description
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class TestListener {

    private final RedisTemplate redisTemplate;

//监听目标队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "test.queue",durable = "true",arguments = @Argument(name = "x-queue-mode",value = "lazy")),
            exchange = @Exchange(name = "test.exchange",type = ExchangeTypes.DIRECT),
            key = "5mm"
    ))
    public void listenerTest(String text){
        log.info("消费者收到消息:{}",text);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "test.queue1",durable = "true",arguments = @Argument(name = "x-queue-mode",value = "lazy")),
            exchange = @Exchange(name = "test.exchange1",type = ExchangeTypes.DIRECT),
            key = "5mm"
    ))
    public void DeadMsgTest(String text){
        log.info("消费者收到消息:{},准备开始测试死信",text);
        throw new RuntimeException("抛出异常，测试死信中");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "test.queue2", durable = "true", arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = "test.ReError"), // 添加x-dead-letter-exchange参数
                    @Argument(name = "x-dead-letter-routing-key",value ="ReError")
            }),
            exchange = @Exchange(name = "test.exchange2", type = ExchangeTypes.DIRECT),
            key = "5mm"
    ))

    public void testReConsume(Message msg){
        String id = msg.getMessageProperties().getMessageId();
        Object a = redisTemplate.opsForValue().get("ConsumedMsg:" + id);
        log.info("消费者尝试处理消息并标记 id:{}, content:{}",id,new String(msg.getBody()), StandardCharsets.UTF_8);
        if(a == null){
            redisTemplate.opsForValue().set("ConsumedMsg:" + id,1);
            log.info("处理消息并标记 id:{}, content:{}",id,new String(msg.getBody()), StandardCharsets.UTF_8);
        }else{
            log.info("信息已处理过！,id:{}",id);
        }
    }
}
