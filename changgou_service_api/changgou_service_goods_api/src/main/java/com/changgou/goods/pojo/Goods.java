package com.changgou.goods.pojo;

import java.util.List;

public class Goods {
    private Spu spu;

    //创建sku集合
    private List<Sku> sku;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSku() {
        return sku;
    }

    public void setSku(List<Sku> sku) {
        this.sku = sku;
    }
}
