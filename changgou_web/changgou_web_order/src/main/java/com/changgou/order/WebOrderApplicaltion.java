package com.changgou.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WebOrderApplicaltion {

    public static void main(String[] args) {
        SpringApplication.run(WebOrderApplicaltion.class,args);
    }
}
