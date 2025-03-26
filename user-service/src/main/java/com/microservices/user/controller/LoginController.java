package com.microservices.user.controller;

import com.microservices.common.constant.ResultStatue;
import com.microservices.user.config.TestHotUpdateConfig;
import com.microservices.user.pojo.dto.LoginFormDTO;
import com.microservices.common.pojo.dto.Result;
import com.microservices.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = "登录接口")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    private TestHotUpdateConfig testHotUpdateConfig;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginFormDTO loginForm) {
        return loginService.login(loginForm);
    }
    @ApiOperation("获取登录消息")
    @GetMapping
    public Result CheakLogin(@RequestParam String username) {
        return loginService.checkLogin(username);
    }

    @ApiOperation("获取登录消息")
    @GetMapping("/get")
    public Result CheakLogin1(@RequestParam String username) {
        return loginService.checkLogin(username);
    }

    @GetMapping("/testHotUpdate")
    public Result testHotUpdate() {
        return new Result(ResultStatue.SUCCESS,"测试热更新",testHotUpdateConfig.getA());
    }

    @GetMapping("/testTransactional")
    public Result testTransactional() {
        loginService.testTransactional();
        return new Result(ResultStatue.SUCCESS,"测试分布事务","OK");
    }

    @PostMapping("/testMessageQueue")
    public Result testMessageQueue(String text) {
        loginService.testMessageQueue(text);
        return new Result(ResultStatue.SUCCESS,"测试消息队列","OK");
    }

    @PostMapping("/testDeadMsg")
    public Result testDeadMsg(String text) {
        loginService.testDeadMsg(text);
        return new Result(ResultStatue.SUCCESS,"测试死信","OK");
    }

    @PostMapping("/testReConsume")
    public Result testReConsume(String text,String id) {
        loginService.testReConsume(text,id);
        return new Result(ResultStatue.SUCCESS,"测试阻止重复消费,id:" + id,"OK");
    }
}
