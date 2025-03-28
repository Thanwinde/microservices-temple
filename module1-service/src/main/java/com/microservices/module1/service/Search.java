package com.microservices.module1.service;

import com.microservices.common.pojo.dto.Result;

import java.io.IOException;

/**
 * @author nsh
 * @data 2025/3/27 19:59
 * @description
 **/
public interface Search {
    Result search(String text) throws IOException;

    Result aggregations(String text,String field) throws IOException;
}
