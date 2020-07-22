package com.changgou.business.listener;

import okhttp3.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdListener {

    @RabbitListener(queues = "ad_update_queue")
    public void receiveMessage(String massage){
        System.out.println("获取到的消息：" + massage);

        //发起远程调用
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://192.168.200.128/ad_update?position="+massage;
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败，发生错误的时候进入该方法
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功，响应数据成功
                System.out.println("请求成功"+response.message());
            }
        });


    }
}
