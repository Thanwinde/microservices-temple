package com.microservices.common.pojo.dto;

import com.microservices.common.constant.ResultStatue;
import lombok.Data;

@Data
public class Result<T> {
    private Integer status = ResultStatue.SUCCESS.getCode();
    private String message;
    private T data;

    // 构造函数
    public Result(ResultStatue status, String message, T data) {
        this.status = status.getCode();
        this.message = message;
        this.data = data;
    }
}
