package com.changgou.pay.service.impl;

import com.changgou.pay.service.WxPayService;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private WXPay wxPay;

    @Value("${wxpay.notify_url}")
    private String notify_url;
    @Override
    public Map nativePay(String orderId, Integer money) {
        try{
            //封装请求参数
            Map<String, String> map = new HashMap();
            map.put("body", "商城");
            map.put("out_trade_no", orderId);
            //微信是以分钱为单位的，所以我们需要进行数据的转换
            BigDecimal payMoney = new BigDecimal("0.01");
            BigDecimal fen = payMoney.multiply(new BigDecimal("100"));//1.00
            fen = fen.setScale(0,BigDecimal.ROUND_UP);//1
            map.put("total_fee",String.valueOf(fen));
            map.put("spbill_create_ip","127.0.0.1");
            map.put("notify_url","www.baidu.com");//回调地址
            map.put("trade_type","NATIVE");//交易类型

            //基于wxpay完成统一下单接口的调用，并返回获取到的结果
            Map<String, String> result = wxPay.unifiedOrder(map);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
