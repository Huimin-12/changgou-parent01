package com.changgou.service;

public interface EsManagerService {

    //创建索引库结构
    void createMappingAndIndex();
    //导入全部数据到ES索引库
    void importAll();
    //根据supId查询skuList导入数据到ES索引库
    void importDataBySupId(String spuid);

}
