package com.changgou.order.service;


import java.util.Map;

public interface CartService {
    void addCart(String skuId,Integer unm,String username);
    Map list(String username);
}
