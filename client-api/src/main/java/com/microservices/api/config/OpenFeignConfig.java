package com.microservices.api.config;

import com.microservices.api.Interceptor.UserSaverInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class OpenFeignConfig {
    @Bean
    public RequestInterceptor UserSaverInterceptor(){
        log.info("添加OpenFeign拦截器");
        return new UserSaverInterceptor();
    }
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
