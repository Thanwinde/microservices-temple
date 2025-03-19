package com.microservices.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.microservices.gateway.config.AuthProperties;
import com.microservices.gateway.util.JWTTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final JWTTool jwtTool;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        String token = httpRequest.getHeaders().getFirst("Authorization");
        if(isContain(httpRequest.getPath().toString())){
            return chain.filter(exchange);
        }
        if(StrUtil.isBlank(token)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }
        token = token.substring(7);
        try {
            Long userid = jwtTool.parseToken(token);
        }catch (Exception e){
            log.error("JWT错误！");
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    private boolean isContain(String path) {
        List<String> excludePaths = authProperties.getExcludePaths();
        for(String excludePath : excludePaths){
            if(path.startsWith(excludePath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
