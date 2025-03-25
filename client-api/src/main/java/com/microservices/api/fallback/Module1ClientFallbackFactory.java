package com.microservices.api.fallback;

import com.microservices.api.client.Module1Client;
import com.microservices.common.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author nsh
 * @data 2025/3/25 18:30
 * @description
 **/
@Slf4j
public class Module1ClientFallbackFactory implements FallbackFactory<Module1Client> {


    @Override
    //设定失败处理
    public Module1Client create(Throwable cause) {
        return new Module1Client() {
            @Override
            public void showUser(Result result) {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + "出错！");
            }

            @Override
            public void testTransactional() {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + "出错！");
            }
        };
    }
}
