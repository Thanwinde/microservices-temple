package com.microservices.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rsa")
public class JWTConfig {
    private String publicKey;
    private String privateKey;
}
