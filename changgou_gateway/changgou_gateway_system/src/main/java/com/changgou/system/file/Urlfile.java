package com.changgou.system.file;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


// 获取客户端的访问url
@Component
public class Urlfile implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("自定义第二个过滤器.....");
        //获取连接对象
        ServerHttpRequest request = exchange.getRequest();
        //获取请求路径
        String path = request.getURI().getPath();
        //输出
        System.out.println("url:"+path);
        //返回，，放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {

        return 2;
    }
}
