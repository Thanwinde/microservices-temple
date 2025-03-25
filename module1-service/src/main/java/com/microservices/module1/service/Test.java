package com.microservices.module1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microservices.module1.pojo.entity.User;

/**
 * @author nsh
 * @data 2025/3/25 16:18
 * @description
 **/
public interface Test extends IService<User> {
    public void testTransactional();
}
