package com.changgou.listener;

import com.changgou.service.EsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsUpListener {

    @Autowired
    private EsManagerService esManagerService;
    public void receiveMessage(String spuId){
        System.out.println("收到的消息为 ： "+spuId);
        //调用service的方法，进行数据的导入索引库
        esManagerService.importDataBySupId(spuId);
    }
}
