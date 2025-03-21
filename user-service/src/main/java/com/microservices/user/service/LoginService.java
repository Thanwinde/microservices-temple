package com.microservices.user.service;

import com.microservices.user.pojo.dto.LoginFormDTO;
import com.microservices.common.pojo.dto.Result;
import com.microservices.user.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LoginService extends IService<User> {
    Result login(LoginFormDTO loginForm);

    Result checkLogin(String username);
}
