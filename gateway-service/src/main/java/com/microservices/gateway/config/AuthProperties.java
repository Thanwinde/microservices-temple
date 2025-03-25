package com.microservices.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("filter")
public class AuthProperties {
    private List<String> excludePaths;
    //会从nacos拉取不过滤api
}

