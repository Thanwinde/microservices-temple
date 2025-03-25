package com.microservices.api.config;

import com.microservices.api.Interceptor.UserSaverInterceptor;
import com.microservices.api.fallback.Module1ClientFallbackFactory;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
//这里是openfeign的配置类
public class OpenFeignConfig {
    @Bean
    //添加拦截器，拦截发出的请求在里面加入user-id请求头，实现传递用户id
    public RequestInterceptor UserSaverInterceptor(){
        log.info("添加OpenFeign拦截器");
        return new UserSaverInterceptor();
    }
    @Bean
    //设定openfeign日志级别，full显示全部日志
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    //这里设定当module1的client出错时该怎么做（回调）
    public Module1ClientFallbackFactory Module1ClientFallbackFactory(){
        return new Module1ClientFallbackFactory();
    }
}
