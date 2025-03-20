package com.microservices.module1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.microservices.api",defaultConfiguration = com.microservices.api.config.OpenFeignConfig.class)
public class Module1Application {
    public static void main(String[] args) {
        SpringApplication.run(Module1Application.class, args);
    }
}
