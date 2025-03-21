package com.microservices.common.config;

import com.microservices.common.Interceptor.UserInfoInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@ConditionalOnClass(DispatcherServlet.class)
public class MVCConfig implements WebMvcConfigurer {
    @Override
    //添加自定义的mvc拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("MVC添加自定义拦截器");
        registry.addInterceptor(new UserInfoInterceptor());
    }
}
