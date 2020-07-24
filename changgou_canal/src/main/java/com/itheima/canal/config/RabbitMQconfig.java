package com.itheima.canal.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 这是一个rabbitmq 配置类
 */
@Configuration
public class RabbitMQconfig {
    //定义交换机
    public static final String GOODS_UP_EXCHANGE="goods_up_exchange";
    public static final String GOODS_DOWN_EXCHANGE="goods_down_exchange";
    //定义队列名称
    public static final String AD_UPDATE_QUEUE="ad_update_queue";
    public static final String SEARCH_ADD_QUEUE="search_add_queue";
    public static final String  SEARCH_DELETE_QUEUE=" search_delete_queue";

    //声明队列
    @Bean
    public Queue queue() {
        return new Queue(AD_UPDATE_QUEUE);
    }
    //声明队列
    @Bean(SEARCH_ADD_QUEUE)//给该队列命名
    public Queue SEARCH_ADD_QUEUE(){
        return new Queue(SEARCH_ADD_QUEUE);
    }
    //声明队列
    @Bean(SEARCH_DELETE_QUEUE)
    public Queue SEARCH_DELETE_QUEUE(){
        return new Queue(SEARCH_DELETE_QUEUE);
    }

    //声明交换机
    @Bean(GOODS_UP_EXCHANGE)
    public Exchange GOODS_UP_EXCHANGE(){
        //GOODS_UP_EXCHANGE:交换机名称 ， true：持久化
        return ExchangeBuilder.directExchange(GOODS_UP_EXCHANGE).durable(true).build();
    }
    //声明交换机
    @Bean()
    public Exchange GOODS_DOWN_EXCHANGE(){
        return ExchangeBuilder.directExchange(GOODS_DOWN_EXCHANGE).durable(true).build();
    }
    //队列与交换机绑定
    @Bean
    public Binding GOODS_UP_EXCHANGE_Binding(@Qualifier(SEARCH_ADD_QUEUE)Queue queue,@Qualifier(GOODS_UP_EXCHANGE)Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
    //队列与交换机绑定
    public Binding GOODS_DOWN_EXCHANGE_Binding(@Qualifier(SEARCH_DELETE_QUEUE)Queue queue,@Qualifier(GOODS_DOWN_EXCHANGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}
