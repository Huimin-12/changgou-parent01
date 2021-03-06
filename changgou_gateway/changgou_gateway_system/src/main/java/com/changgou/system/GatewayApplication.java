package com.changgou.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {
    public static void main(String[] args) {
       SpringApplication.run(GatewayApplication.class,args);
    }

    //网关限流
    @Bean
    public KeyResolver ipKeyResolver(){
        return new KeyResolver() {
             @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //获取客户端请求id,并返回
                return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
            }
        };
    }
}
