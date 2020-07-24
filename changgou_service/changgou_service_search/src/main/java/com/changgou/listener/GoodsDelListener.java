package com.changgou.listener;

import com.changgou.config.RabbitMQconfig;
import com.changgou.service.EsManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsDelListener {
    @Autowired
    private EsManagerService esManagerService;

    @RabbitListener(queues = RabbitMQconfig.SEARCH_DELETE_QUEUE)
    public void receiveMessage(String skuId){

        System.out.println("删除索引库,接收到的消息未 ： "+skuId);
        //调用业务层方法，完成索引库数据删除
      esManagerService.delDataBySpuId(skuId);
    }
}
