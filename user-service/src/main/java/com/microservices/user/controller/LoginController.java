package com.microservices.user.controller;


import com.microservices.user.pojo.dto.LoginFormDTO;
import com.microservices.common.pojo.dto.Result;
import com.microservices.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = "登录接口")
public class LoginController {

    private final LoginService loginService;

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
}
