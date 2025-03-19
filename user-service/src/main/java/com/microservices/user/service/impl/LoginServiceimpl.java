package com.microservices.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservices.common.constant.ResultStatue;
import com.microservices.user.mapper.UserMapper;
import com.microservices.user.pojo.dto.LoginFormDTO;
import com.microservices.common.pojo.dto.Result;
import com.microservices.user.pojo.entity.User;
import com.microservices.user.service.LoginService;
import com.microservices.user.util.JWTTool;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class LoginServiceimpl extends ServiceImpl<UserMapper,User> implements LoginService {

    public final JWTTool jwtTool;

    public final RedisTemplate redisTemplate;

    @Override
    public Result<String> login(LoginFormDTO loginForm) {
        User user = lambdaQuery().eq(User::getUsername,loginForm.getUsername()).one();
        if(user == null){
            return new Result(ResultStatue.ERROR,"无此用户!",null);
        }
        if(user.getPassword().equals(loginForm.getPassword())){
            String token = jwtTool.createToken(user.getId(), Duration.ofMinutes(10L));
            return new Result(ResultStatue.SUCCESS,"成功",token);
        }
        return new Result(ResultStatue.ERROR,"成功",null);
    }

    @Override
    public Result checkLogin(String username) {
        User user = lambdaQuery().eq(User::getUsername,username).one();
        if(user == null){
            return new Result(ResultStatue.ERROR,"无此用户!",null);
        }
        return new Result(ResultStatue.SUCCESS,"成功",user);
    }
}
