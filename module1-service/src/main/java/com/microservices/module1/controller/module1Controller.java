package com.microservices.module1.controller;

import com.microservices.common.pojo.dto.Result;
import com.microservices.common.util.UserContext;
import com.microservices.module1.service.Search;
import com.microservices.module1.service.Test;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/module1")
@RequiredArgsConstructor
@Api(tags = "module1接口")
public class module1Controller {

    private final Test test;

    private final Search search;

    @PostMapping("/show")
    public void showUser(@RequestBody Result result) {
        System.out.println("当前用户:" + UserContext.getUserId());
        System.out.println(result);
    }
    @GetMapping("/testTransactional")
    public void testTransactional() {
        test.testTransactional();
    }
    //测试elasticsearch
    @GetMapping("/search")
    public Result search(@RequestParam String text) throws IOException {
        return search.search(text);
    }
    //测试elasticsearch聚合搜索
    @GetMapping("/agg")
    public Result agg(@RequestParam String text,@RequestParam String field) throws IOException {
        return search.aggregations(text,field);
    }
}
