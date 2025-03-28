package com.microservices.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservices.api.client.Module1Client;
import com.microservices.common.constant.ResultStatue;
import com.microservices.user.mapper.UserMapper;
import com.microservices.user.pojo.dto.LoginFormDTO;
import com.microservices.common.pojo.dto.Result;
import com.microservices.user.pojo.entity.User;
import com.microservices.user.service.LoginService;
import com.microservices.user.util.JWTTool;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@Slf4j
@RequiredArgsConstructor
@GlobalTransactional
public class LoginServiceimpl extends ServiceImpl<UserMapper,User> implements LoginService {

    public final JWTTool jwtTool;

    public final RedisTemplate redisTemplate;

    public final Module1Client module1Client;

    public final RabbitTemplate rabbitTemplate;

    private ExecutorService executorService;
    //线程池
    @PostConstruct
    public void init() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public Result<String> login(LoginFormDTO loginForm) {
        User user = lambdaQuery().eq(User::getUsername,loginForm.getUsername()).one();
        if(user == null){
            return new Result(ResultStatue.ERROR,"无此用户!",null);
        }
        if(user.getPassword().equals(loginForm.getPassword())){
            String token = jwtTool.createToken(user.getId(), Duration.ofDays(10L));
            return new Result(ResultStatue.SUCCESS,"成功",token);
        }
        return new Result(ResultStatue.ERROR,"成功",null);
    }

    @Override
    public Result checkLogin(String username) {
        User user = lambdaQuery().eq(User::getUsername,username).one();
        if(user == null){
            return new Result(ResultStatue.ERROR,"无此用户!",null);
        }
        module1Client.showUser(new Result(ResultStatue.SUCCESS,"成功",user));
        return new Result(ResultStatue.SUCCESS,"成功",user);
    }

    @Override
    public void testTransactional() {
        log.info("事务开始");
        module1Client.testTransactional();
    }

    @Override
    public void testMessageQueue(String text) {
        log.info("生产者尝试发出消息:{}",text);
        try {
            rabbitTemplate.convertAndSend("test.exchange", "5mm", text);
        }catch (Exception e){
            log.error("消息发送失败, 启动重试线程: {}", e.getMessage());
            // 启动一个新的线程来处理重试逻辑
            executorService.submit(() -> retrySendingMessage(text, 3));
        }
    }

    @Override
    public void testDeadMsg(String text) {
        rabbitTemplate.convertAndSend("test.exchange1", "5mm", text);
    }

    @Override
    public void testReConsume(String text,String id) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(id);
        messageProperties.setContentEncoding("UTF-8");
        Message message = new Message(text.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("test.exchange2", "5mm", message);
    }

    @Override
    public void delayQueue(String text) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setDelay(5000);//延时5秒
        Message message = new Message(text.getBytes(), messageProperties);
        log.info("发送延时队列，延时五秒");
        rabbitTemplate.convertAndSend("test.delayExchange", "5mm", message);
    }

    private void retrySendingMessage(String text, int maxAttempts) {
        int attempts = 0;
        boolean sent = false;
        while (attempts < maxAttempts && !sent) {
            try {
                // 尝试发送消息
                rabbitTemplate.convertAndSend("test.exchange", "5mm", text);
                log.info("消息发送成功: {}", text);
                sent = true; // 发送成功，退出循环
            } catch (Exception e) {
                attempts++;
                log.error("消息发送失败，第{}次重试: {}", attempts, e.getMessage());
                if (attempts == maxAttempts) {
                    log.error("消息发送失败，已尝试{}次，放弃发送", maxAttempts);
                }
                try {
                    // 等待一段时间再重试，避免频繁重试
                    Thread.sleep(1000); // 等待1秒
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
