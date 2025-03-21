package com.microservices.common.Interceptor;

import com.microservices.common.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    //先于方法执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userid = request.getHeader("user-id");
        if(userid != null) {
            log.info("MVC存储user-id数据到线程:{}", userid);
            UserContext.setUserId(userid);
        }
        return true;
    }

    @Override
    //在执行完之后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUserId();
    }
}
