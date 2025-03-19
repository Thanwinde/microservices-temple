package com.microservices.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
public class AuthProperties {
    //@Value("${filter.includePaths}")
    //private List<String> includePaths;
    @Value("${filter.excludePaths}")
    private List<String> excludePaths;
}

