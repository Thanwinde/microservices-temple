package com.microservices.api.client;

import com.microservices.common.pojo.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient("module1-service")
//module1-service模块对应的client
public interface Module1Client {
    @PostMapping("/module1/show")
    public void showUser(@RequestBody Result result);
    @GetMapping("/module1/testTransactional")
    public void testTransactional();
}
