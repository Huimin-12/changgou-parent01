package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    //查询购物车中存在的商品
    @GetMapping("/list")
    public String list(Model model){
        Map map = cartFeign.list();
        model.addAttribute("items",map);
        return "cart";
    }

    //添加购物车
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> addCart(@RequestParam("spuId")String spuId, @RequestParam("num") Integer num){
        cartFeign.add(spuId,num);
        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }
}
