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
    //路由管理器
    private final RouteDefinitionWriter writer;

    //nacos配置管理器
    private final NacosConfigManager nacosConfigManager;

    private String dataId = "gateway-routes.json";
    //nacos拉取的路由文件名
    private String group = "DEFAULT_GROUP";
    //nacos分组
    private HashSet<String> OldRoutes = new HashSet<>();
    //记录上一次的老路由，方便删除


    @PostConstruct
    public void init() throws NacosException {
        //手动从nacos拉取路由配置
        //getConfigAndSignListener既可以拉取配置又可以设置监听器(响应配置更改)
        String config = nacosConfigManager.getConfigService().getConfigAndSignListener(
                dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return Executors.newFixedThreadPool(3);
                    }//开多个线程，以便监听到配置改变时快速修改

                    @Override
                    //这里监听到配置改变了就会来调用这个方法
                    public void receiveConfigInfo(String s) {
                        updateConfigInfo(s);
                    }
                }
        );
        updateConfigInfo(config);
    }
    private void updateConfigInfo(String config) {
        //把传过来的配置解析成RouteDefinition（即路由类）
        List<RouteDefinition> routes = JSONUtil.toList(config, RouteDefinition.class);
        OldRoutes.forEach(
                //对照着老路由删除
                (a) -> writer.delete(Mono.just(a)).subscribe());
        OldRoutes.clear();
        for (RouteDefinition route : routes) {
            //存储新路由并保存
            writer.save(Mono.just(route)).subscribe();
            OldRoutes.add(route.getId());
        }

    }

}
