package com.microservices.module1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservices.module1.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author nsh
 * @data 2025/3/25 16:26
 * @description
 **/
@Mapper
public interface TestMapper extends BaseMapper<User> {
}
