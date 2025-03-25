package com.microservices.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservices.user.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
