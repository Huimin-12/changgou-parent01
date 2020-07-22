package com.itheima.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.itheima.canal.config.RabbitMQconfig;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ZJ
 */
@CanalEventListener
public class BusinessListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ListenPoint(schema = "changgou_business", table = {"tb_ad"})
    public void adUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("广告数据发生变化");
            //遍历拿到修改后的数据
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            //判断为 position 的数据
            if ("position".equals(column.getName())){
                System.out.println("发送最新的消息到MQ："+ column.getValue());
                //发送消息---交换机名称，队列名称，发送的消息
                rabbitTemplate.convertAndSend("", RabbitMQconfig.AD_UPDATE_QUEUE,column.getValue());
            }
        }
        //测试代码
      /*  //修改前数据
        for(CanalEntry.Column column: rowData.getBeforeColumnsList()) {
            if(column.getName().equals("position")){
                System.out.println("发送消息到mq  ad_update_queue:"+column.getValue());
                rabbitTemplate.convertAndSend("","ad_update_queue",column.getValue());  //发送消息到mq
                break;
            }
        }
        //使用Lambda表达式
        rowData.getBeforeColumnsList().forEach(c-> System.out.println("数据改变前数据："+c.getName()+"::"+c.getValue()));
        System.out.println("-----------------------------------");
        //修改后数据
        for(CanalEntry.Column column: rowData.getAfterColumnsList()) {
            if(column.getName().equals("position")){
                System.out.println("发送消息到mq  ad_update_queue:"+column.getValue());
                rabbitTemplate.convertAndSend("","ad_update_queue",column.getValue());  //发送消息到mq
                break;
            }
        }*/
    }
}
