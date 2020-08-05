package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.AddressFeign;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/worder")
public class OrderController {
    @Autowired
    private AddressFeign addressFeign;

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private OrderFeign orderFeign;

    //订单结算跳转
    @GetMapping("/ready/order")
    public String readyOrder(Model model) {
        //查询用户地址
        List<Address> addressList = addressFeign.list().getData();
        //遍历用户地址，设置其默认值
        for (Address address : addressList) {
            //进行判断
            if (address.getIsDefault().equals("1")) {
                model.addAttribute("address", address);
                break;
            }
        }
        model.addAttribute("address", addressList);

        //使用cartFeign调用查询购物车商品数据
        Map cartMap = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>) cartMap.get("orderItemList");
        Integer totalMoney = (Integer) cartMap.get("totalMoney");
        Integer totalNum = (Integer) cartMap.get("totalNum");

        model.addAttribute("carts", orderItemList);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("totalNum", totalNum);
        return "order";
    }

    @PostMapping("/add")
    public Result add(@RequestBody Order order) {
        Result result = orderFeign.add(order);
        return result;
    }

    @GetMapping("/findByOrderId")
    public String findByOrderId(@RequestParam("findByOrderId") String findByOrderId, Model model) {
        Result<Order> orderResult = orderFeign.findByOrderId(findByOrderId);
        Order order = orderResult.getData();

        model.addAttribute("orderId", findByOrderId);
        model.addAttribute("payMoney",order.getPayMoney() );

        return "pay";
    }
}
