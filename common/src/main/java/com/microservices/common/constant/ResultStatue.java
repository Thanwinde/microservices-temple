package com.microservices.common.constant;

public enum ResultStatue {
    SUCCESS(200),ERROR(500),NOT_FOUND(404),UNAUTHORIZED(401),FORBIDDEN(403);
    private final int code;
    ResultStatue(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
