package com.itheima.canal.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.Query;

/**
 * 这是一个rabbitmq 配置类
 */
@Configuration
public class RabbitMQconfig {

    //定义队列名称
    public static final String AD_UPDATE_QUEUE="ad_update_queue";

    //声明队列
    @Bean
    public Queue queue() {
        return new Queue(AD_UPDATE_QUEUE);
    }
}
