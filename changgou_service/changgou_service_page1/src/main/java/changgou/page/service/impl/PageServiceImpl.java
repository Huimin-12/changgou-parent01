package changgou.page.service.impl;

import changgou.page.service.PageService;
import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PageServiceImpl implements PageService {
    @Value("${pagepath}")
    private String pagepath;
    @Autowired
    private TemplateEngine templateEngine;
    //生成静态化页面
    @Override
    public void generateItemPage(String spuId) {
        //获取context对象，用于存储商品信息
        Context context = new Context();
        //调用方法，查询静态化页面所需数据
        Map<String,Object> itemDate =this.finditemData(spuId);
        context.setVariables(itemDate);
        //获取商品详情页生成的指定位置
        File dir = new File(pagepath);
        //判断存储位置文件夹是否存在，不存在，则创建
        if (!dir.exists()){
            dir.mkdirs();
        }
        //定义输出流，完成文件的生成
        File file = new File(dir+"/"+spuId+".html");
        Writer out = null;
        try {
            new PrintWriter(file);
            //生成文件
            /*** 1.模板名称 * 2.context对象,包含了模板需要的数据 * 3.输出流,指定文件输出位置 */
            templateEngine.process("item",context,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            //关闭流
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private CategoryFeign categoryFeign;
    @Autowired
    private SkuFeign skuFeign;
    //获取静态化页面相关数据
    private Map<String, Object> finditemData(String spuId) {
        //创建一个map集合
        Map<String,Object> resultMap = new HashMap<>();
        //使用feign的远程调用
        Spu spu = spuFeign.findById(spuId).getData();
        resultMap.put("spu",spu);
        //获取图片信息
        if (spu!=null){
            if (StringUtils.isNotEmpty(spu.getImages())){
                //存储图片信息
                resultMap.put("images",spu.getImages().split(","));
            }
        }
        //获取商品的分类信息
        Category category1 = categoryFeign.findById(spu.getCategory1Id()).getData();
        resultMap.put("category1",category1);

        Category category2 = categoryFeign.findById(spu.getCategory2Id()).getData();
        resultMap.put("category2",category2);

        Category category3 = categoryFeign.findById(spu.getCategory3Id()).getData();
        resultMap.put("category3",category3);
        //获取sku的相关信息
        List<Sku> skuList = skuFeign.spuId(spuId);
        resultMap.put("skuList",skuList);

        //获取商品规格信息
        resultMap.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        return resultMap;
    }
}
