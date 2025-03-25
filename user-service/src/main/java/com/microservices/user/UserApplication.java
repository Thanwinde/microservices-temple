package com.microservices.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.microservices.api",defaultConfiguration = com.microservices.api.config.OpenFeignConfig.class)
//对openFeign就行配置，basePackages指对应client类，defaultConfiguration指对当前client的配置
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
