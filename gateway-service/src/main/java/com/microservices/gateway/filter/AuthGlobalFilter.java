package com.microservices.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.microservices.gateway.config.AuthProperties;
import com.microservices.gateway.util.JWTTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final JWTTool jwtTool;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst("Authorization");
        if(isContain(request.getPath().toString())){
            return chain.filter(exchange);
        }
        if(StrUtil.isBlank(token)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }
        token = token.substring(7);
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        }catch (Exception e){
            log.error("JWT错误！");
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            String message = "令牌错误或过期!";
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
            response.writeWith(Mono.just(dataBuffer));
            return response.setComplete();
        }
        String id = userId.toString();
        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("user-id", id)  // 添加新的请求头
                .build();
        log.info("已添加user-id请求头:{}",id);
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
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
