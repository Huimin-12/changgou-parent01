package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.changgou.util.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper; //品牌mapper

    @Autowired
    private CategoryMapper categoryMapper; //分类mapper

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private GategoryBrandMapper gategoryBrandMapper; //分类与品牌关系mapper
    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id){
        return  spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param goods
     */
    @Autowired
    private IdWorker idWorker;
    @Transactional //事务注解
    @Override
    public void add(Goods goods){
        //添加spu
        Spu spu = goods.getSpu();
        long idworker = idWorker.nextId();
        //设置商品分布式id
        spu.setId(String.valueOf(idworker));
        spu.setIsDelete("0");//是否删除了
        spu.setIsMarketable("0");//是否上架了
        spu.setStatus("0");

        spuMapper.insertSelective(spu);

        //添加sku集合
       saveSkuList(goods);

    }

    /**
     * 添加sku集合信息
     * @param goods
     */
    private void saveSkuList(Goods goods) {
        //获取spu对象
        Spu spu = goods.getSpu();
        //获取当前日期
        Date date = new Date();
        //获取品牌对象
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        //获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //创建类和品牌关系
        GategoryBrand gategoryBrand = new GategoryBrand();
        gategoryBrand.setBrandId(spu.getBrandId());
        gategoryBrand.setCategoryId(spu.getCategory3Id());
        //查询数据库表中是否已经存在关系
        int gategCount = gategoryBrandMapper.selectCount(gategoryBrand);
        //不存在关联关系，就添加其关联关系
        if (gategCount==0){
            gategoryBrandMapper.insertSelective(gategoryBrand);
        }
        //获取sku集合对象
        List<Sku> skus = goods.getSku();
        if (skus!=null) {
            for (Sku sku : skus) {
                //设置sku主键id
                sku.setId(String.valueOf(idWorker.nextId()));
                //设置sku规格
                if (sku.getSpec() == null || sku.getSpec().length() == 0 || "".equals(sku.getSpec())) {
                    //说明没有规格参数，自动给出默认参数为“{}”
                    sku.setSpec("{}");
                }
                //设置sku名称（商品名称+规格名称）
                String name = spu.getName();
                //将规格json格式转换成string
                Map<String,String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
                //确保map集合当中是有数据存在的
                if (specMap!=null&&specMap.size()>0){
                    for (String value : specMap.values()) {
                        //把获取到的规格值追加到name
                        name+="" + value;
                    }
                }
                sku.setName(name);//sku的名称
                sku.setSpec(spu.getId());//设置spu的id
                sku.setCreateTime(date);//创建日期
                sku.setUpdateTime(date);//修改日期
                sku.setCategoryId(category.getId());//商品分类id
                sku.setCategoryName(category.getName());//商品分类名称
                sku.setBrandName(brand.getName());//品牌名称
                skuMapper.insertSelective(sku);//插入sku表格
            }
        }

    }


    /**
     * 修改
     * @param goods
     */
    @Override
    public void update(Goods goods){
        Spu spu = goods.getSpu();
        //修改spu
        spuMapper.updateByPrimaryKey(spu);
        //删除sku
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spu.getId());
        //删除
        skuMapper.deleteByExample(example);
        //重新添加sku
        saveSkuList(goods);

    }

    /**
     * 逻辑删除
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id){
        //逻辑删除
        //查询出spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!spu.getIsMarketable().equals("0")){
            throw new RuntimeException("商品必须先下架，才可以删除");
        }
        //
        spu.setIsDelete("1");//修改为已删除状态
        spu.setStatus("0");//修改为未审核状态
        //xxxSelective数据为null则不修改表中数据，xxx为null则修改表中数据为null
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 恢复数据
     * @param id
     */
    @Transactional
    @Override
    public void restore(String id) {
        //查询出spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!spu.getIsMarketable().equals("1")){
            throw new RuntimeException("该商品未删除");
        }
        spu.setIsDelete("0");//修改为未删除状态
        spu.setStatus("0");//修改为未审核状态
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 物理删除--彻底删除数据
     * @param id
     */
    @Override
    public void realDelete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!spu.getIsMarketable().equals("1")){
            throw new RuntimeException("该商品未删除");
        }
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Spu>)spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Spu>)spuMapper.selectByExample(example);
    }

    @Override
    public Goods findGoodsById(String id) {
        //创建Goods对象
        Goods goods = new Goods();
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //查询sku
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        List<Sku> skus = skuMapper.selectByExample(example);
        //封装到goods当中
        goods.setSpu(spu);
        goods.setSku(skus);
        //返回封装好的数据
        return goods;
    }

    /**
     * 审核，商品上架
     * @param id
     */
    @Transactional
    @Override
    public void audit(String id) {
        //根据id 查询spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu==null){
            //当前商品不存在
            throw  new RuntimeException("当前商品不存在");
        }
        if ("1".equals(spu.getIsDelete())){
            //判断当前删除id是否是1，1代表该商品已经删除
            throw new RuntimeException("商品已经删除");
        }
        //没有进if说明该商品存在，审核通过，商品上架
        spu.setStatus("1");
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品下架
     * @param id
     */
    @Override
    public void pull(String id) {
        //根据id查询spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu==null){
            //当前商品不存在
            throw  new RuntimeException("当前商品不存在");
        }
        if ("1".equals(spu.getIsDelete())){
            //判断当前删除id是否是1，1代表该商品已经删除
            throw new RuntimeException("商品已经删除");
        }
        //商品可以下架
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品上架
     * @param id
     */
    @Override
    public void put(String id) {
        //根据id查询spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null){
            //当前商品不存在
            throw  new RuntimeException("当前商品不存在");
        }
        if (!spu.getStatus().equals("1")){
            //该商品审核未通过
            throw new RuntimeException("商品未通过审核");
        }
        if ("1".equals(spu.getIsDelete())){
            //判断当前删除id是否是1，1代表该商品已经删除
            throw new RuntimeException("商品已经删除");
        }
        //商品可以上架
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 主键
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
           	}
            // 货号
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andEqualTo("sn",searchMap.get("sn"));
           	}
            // SPU名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 副标题
            if(searchMap.get("caption")!=null && !"".equals(searchMap.get("caption"))){
                criteria.andLike("caption","%"+searchMap.get("caption")+"%");
           	}
            // 图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
           	}
            // 图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
           	}
            // 售后服务
            if(searchMap.get("saleService")!=null && !"".equals(searchMap.get("saleService"))){
                criteria.andLike("saleService","%"+searchMap.get("saleService")+"%");
           	}
            // 介绍
            if(searchMap.get("introduction")!=null && !"".equals(searchMap.get("introduction"))){
                criteria.andLike("introduction","%"+searchMap.get("introduction")+"%");
           	}
            // 规格列表
            if(searchMap.get("specItems")!=null && !"".equals(searchMap.get("specItems"))){
                criteria.andLike("specItems","%"+searchMap.get("specItems")+"%");
           	}
            // 参数列表
            if(searchMap.get("paraItems")!=null && !"".equals(searchMap.get("paraItems"))){
                criteria.andLike("paraItems","%"+searchMap.get("paraItems")+"%");
           	}
            // 是否上架
            if(searchMap.get("isMarketable")!=null && !"".equals(searchMap.get("isMarketable"))){
                criteria.andEqualTo("isMarketable",searchMap.get("isMarketable"));
           	}
            // 是否启用规格
            if(searchMap.get("isEnableSpec")!=null && !"".equals(searchMap.get("isEnableSpec"))){
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
           	}
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
           	}
            // 审核状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
           	}

            // 品牌ID
            if(searchMap.get("brandId")!=null ){
                criteria.andEqualTo("brandId",searchMap.get("brandId"));
            }
            // 一级分类
            if(searchMap.get("category1Id")!=null ){
                criteria.andEqualTo("category1Id",searchMap.get("category1Id"));
            }
            // 二级分类
            if(searchMap.get("category2Id")!=null ){
                criteria.andEqualTo("category2Id",searchMap.get("category2Id"));
            }
            // 三级分类
            if(searchMap.get("category3Id")!=null ){
                criteria.andEqualTo("category3Id",searchMap.get("category3Id"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
            // 运费模板id
            if(searchMap.get("freightId")!=null ){
                criteria.andEqualTo("freightId",searchMap.get("freightId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }

}
