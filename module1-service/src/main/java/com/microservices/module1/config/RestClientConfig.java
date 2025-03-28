package com.microservices.module1.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author nsh
 * @data 2025/3/27 20:07
 * @description
 **/
@Configuration
public class RestClientConfig {

    //通过这个配置类来连接elasticsearch，可以考虑改成热配置

    private RestHighLevelClient client;

    @PostConstruct
    public void init() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.247.133:9200")));
    }

    public RestHighLevelClient getClient() {
        return client;
    }
}
