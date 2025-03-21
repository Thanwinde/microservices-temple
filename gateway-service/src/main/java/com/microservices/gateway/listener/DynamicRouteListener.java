package com.microservices.gateway.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class DynamicRouteListener {
    private final RouteDefinitionWriter writer;

    private final NacosConfigManager nacosConfigManager;

    private String dataId = "gateway-routes.json";
    private String group = "DEFAULT_GROUP";
    private HashSet<String> OldRoutes = new HashSet<>();


    @PostConstruct
    public void init() throws NacosException {
        String config = nacosConfigManager.getConfigService().getConfigAndSignListener(
                dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return Executors.newFixedThreadPool(3);
                    }

                    @Override
                    public void receiveConfigInfo(String s) {
                        updateConfigInfo(s);
                    }
                }
        );
        updateConfigInfo(config);
    }
    private void updateConfigInfo(String config) {
        List<RouteDefinition> routes = JSONUtil.toList(config, RouteDefinition.class);
        OldRoutes.forEach(
                (a) -> writer.delete(Mono.just(a)).subscribe());
        OldRoutes.clear();
        for (RouteDefinition route : routes) {
            writer.save(Mono.just(route)).subscribe();
            OldRoutes.add(route.getId());
        }

    }

}
