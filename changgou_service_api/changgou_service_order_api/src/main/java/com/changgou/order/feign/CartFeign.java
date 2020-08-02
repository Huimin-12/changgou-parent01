package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@FeignClient(name = "order")
public interface CartFeign {
    //添加购物车
    @GetMapping("/cart/add")
    @ResponseBody
    Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num);

    //查询购物车
    @GetMapping("/cart/list")
    @ResponseBody
    Map list();
}
