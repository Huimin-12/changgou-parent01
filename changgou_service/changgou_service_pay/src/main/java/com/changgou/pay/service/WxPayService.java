package com.changgou.pay.service;

import java.util.Map;

public interface WxPayService {
    Map nativePay(String orderId,Integer money);
}
