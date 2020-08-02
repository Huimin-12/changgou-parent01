package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private static final String CART="cart_";
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;
    /**
     * 添加购物车方法
     * @param skuId
     * @param num
     * @param username
     */
    @Override
    public void addCart(String skuId, Integer num, String username) {

        /**
         * 查询redis当中的数据
         * 如果redis当中有缓存的数据，就是用redis当中的数据
         * 没有数据的话查询数据库将数据缓存到redis
         */
        //从redis当中查询数据
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuId);
        if (orderItem!=null){
            //说明redis当中有缓存的数据，直接购物车数量和价钱
            orderItem.setNum(orderItem.getNum()+num);
            if (orderItem.getNum()<=0){
                //说明该商品数量等于0，需要移除该商品
                redisTemplate.boundHashOps(CART+username).delete(skuId);
            }
            orderItem.setMoney(orderItem.getNum()*orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum()*orderItem.getPrice());
        }else {
            //如果redis当中没有缓存的数据
            Sku sku = skuFeign.findById(skuId).getData();
            Spu spu = spuFeign.findById(sku.getSpuId()).getData();
            orderItem = this.sku2OrderItem(sku, spu, num);


        }
        //存入redis
        redisTemplate.boundHashOps(CART+username).put(skuId,orderItem);


    }
    //查询购物车列表数据
    @Override
    public Map list(String username) {
        Map map = new HashMap();

        List<OrderItem> orderItemList = redisTemplate.boundHashOps(CART + username).values();
        map.put("orderItemList", orderItemList);

        //商品的总数量与总价格
        Integer totalNum = 0;
        Integer totalMoney = 0;

        for (OrderItem orderItem : orderItemList) {
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getMoney();
        }

        map.put("totalNum", totalNum);
        map.put("totalMoney", totalMoney);

        return map;
    }
    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(orderItem.getPrice()*num);
        orderItem.setPayMoney(orderItem.getPrice()*num);
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);
        //分类信息
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
