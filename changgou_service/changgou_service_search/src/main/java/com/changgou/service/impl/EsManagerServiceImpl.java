package com.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.service.EsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsManagerServiceImpl implements EsManagerService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SkuFeign skuFeign;
    //创建索引库结构
    @Override
    public void createMappingAndIndex() {
        //创建索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        //创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }
    //导入全部数据到ES索引库
    @Override
    public void importAll() {
        //查询所有的数据
        List<Sku> skuList = skuFeign.spuId("All");
        //判断是否有数据
        if (skuList==null){
            new RuntimeException("当前数据异常，请稍后重试");
        }
        //将skuList转换成json
        String jsonSkuList = JSON.toJSONString(skuList);
        //将json转换为skuInfo
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
        //遍历集合
        for (SkuInfo skuInfo : skuInfoList) {

        }


    }
    //根据supId查询skuList导入数据到ES索引库
    @Override
    public void importDataBySupId(String spuid) {

    }
}
