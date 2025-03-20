package com.microservices.api.Interceptor;

import com.microservices.common.util.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSaverInterceptor implements RequestInterceptor {
    public void apply(RequestTemplate template) {
        log.info("UserSaverInterceptor apply");
        String userId = UserContext.getUserId();
        if(userId != null) {
            log.info("OpenFeign存储user-id到请求头: {}", userId);
            template.header("user-id", userId);
        }
        return;
    }
}
