package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods")
public interface SkuFeign {
    /**
     * 多条件搜索品牌数据
     * @param spuId
     * @return
     */
    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> spuId(@PathVariable("spuId") String spuId);

    /**
     * 根据skuid查询sku的信息
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);

    /**
     * 递归删除库存
     * @param username
     * @return
     */
    @PostMapping("/sku/decr/count")
    public Result decrCount(@RequestParam("username") String username);
}
