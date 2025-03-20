package com.microservices.module1.controller;

import com.microservices.common.constant.ResultStatue;
import com.microservices.common.pojo.dto.Result;
import com.microservices.module1.pojo.entity.User;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/module1")
@RequiredArgsConstructor
@Api(tags = "module1接口")
public class module1Controller {
    @PostMapping("/show")
    public void showUser(@RequestBody Result result) {
        System.out.println(result);
        //return new Result(ResultStatue.SUCCESS,"Hello!",result);
    }
}
