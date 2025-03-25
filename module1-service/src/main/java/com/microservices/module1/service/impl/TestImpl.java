package com.microservices.module1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservices.module1.mapper.TestMapper;
import com.microservices.module1.pojo.entity.User;
import com.microservices.module1.service.Test;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author nsh
 * @data 2025/3/25 16:18
 * @description
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class TestImpl extends ServiceImpl<TestMapper,User> implements Test {
    @Override
    public void testTransactional() {
        User user = lambdaQuery().eq(User::getUsername,"wyh").one();
        log.info("事务开始前余额：{}",user.getBalance());
        user.setBalance(191918);
        log.info("现在余额{}",191918);
        lambdaUpdate().eq(User::getUsername,"wyh").update();
        log.info("修改余额：{}",user.getBalance());
        throw new RuntimeException("事务抛出报错！准备回滚");
    }
}
