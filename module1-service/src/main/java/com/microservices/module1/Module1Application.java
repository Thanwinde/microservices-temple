package com.microservices.module1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.microservices.api",defaultConfiguration = com.microservices.api.config.OpenFeignConfig.class)
//对openFeign就行配置，basePackages指对应client类，defaultConfiguration指对当前client的配置
public class Module1Application {
    public static void main(String[] args) {
        SpringApplication.run(Module1Application.class, args);
    }
}
