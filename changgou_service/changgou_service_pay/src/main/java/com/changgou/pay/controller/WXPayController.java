package com.changgou.pay.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.pay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/wxpay")
public class WXPayController {

    @Autowired
    private WxPayService wxPayService;

    /**
     * 下单，调用微信下单接口，返回二维码
     * @param orderId
     * @param money
     * @return
     */
    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId")String orderId,@RequestParam("money")Integer money){

        Map map = wxPayService.nativePay(orderId, money);

        return new Result(true, StatusCode.OK,"",map);
    }

}
