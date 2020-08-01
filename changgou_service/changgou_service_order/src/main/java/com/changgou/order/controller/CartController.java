package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/add")
    @ResponseBody
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num){

        String username = "xiaomin";
        cartService.addCart(skuId,num,username);
        return new Result(true, StatusCode.OK,"购物车添加成功");
    }

    @GetMapping("/list")
    @ResponseBody
    public Map list(){
        String username = "xiaomin";
        Map map = cartService.list(username);
        return map;
    }
}
