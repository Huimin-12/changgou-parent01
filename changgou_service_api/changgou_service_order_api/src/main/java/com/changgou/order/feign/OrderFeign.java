package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order")
public interface OrderFeign {

    //提交订单数据
    @PostMapping("/order")
    public Result add(@RequestBody Order order);

    @GetMapping("/order/findByOrderId")
    Result findByOrderId (@RequestParam("orderId") String orderId);
}
