package com.changgou.system.file;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
//获取客户端访问的ip
@Component
public class Ipfile implements Ordered, GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("自定义第一个过滤器....");
        //获取连接对象
        ServerHttpRequest request = exchange.getRequest();
        //获取remoteAddress
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        //输出ip地址
        System.out.println("ip:"+remoteAddress.getHostName());
        //返回，，放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {

        return 1;
    }
}
