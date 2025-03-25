package com.microservices.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rsa")
//可热更新
@Data
public class JWTConfig {
    private String publicKey;
    private String privateKey;
    //从nacos拉取密钥文件
}
