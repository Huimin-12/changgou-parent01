package com.changgou.controller;


import com.changgou.entity.Page;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @GetMapping("/list")
    public String list(@RequestParam Map<String,String> searchMap, Model model){

        //特殊符号的处理
        Map<String,Object> resultMap = searchService.search(searchMap);

        model.addAttribute("searchMap",searchMap);
        model.addAttribute("resultMap",resultMap);
        //封装分页数据并返回
        /**
         * 所需三个参数：总记录数，当前页，每页显示条数
         */
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(searchMap.get("total")),
                Integer.parseInt(String.valueOf(searchMap.get("pageNum"))),
                Page.pageSize
        );
        model.addAttribute("page",page);
        //拼装url
        StringBuilder url = new StringBuilder("/search/list");
        //拼接字符串
        url.append("?");
        if (searchMap!=null && searchMap.size()>0) {
            //遍历传入过来的map集合
            for (String paramKey : searchMap.keySet()) {
                if (!"sortRule".equals(paramKey) && !"sortField".equals(paramKey) && !"pageNum".equals(paramKey)) {
                    //路径的拼接
                    url.append(paramKey).append("=").append(searchMap.get(paramKey)).append("&");
                }
            }
        }
        //拼接完成之后变成的路径： http://localhost:9009/search/list?keywords=电视&spec_网络制式=4G&
        String urlString = url.toString();
        //去除路径上最后一个 & 符号
        urlString=urlString.substring(0,urlString.length()-1);
        //把路径使用模板返回
        model.addAttribute("url",urlString);
        return "search";
    }

    @ResponseBody
    @GetMapping
    public Map search(@RequestParam Map<String,String> searchMap){
        //特殊符号处理
        this.handleSearchMap(searchMap);
        Map searchResult = searchService.search(searchMap);
        return searchResult;
    }

    private void handleSearchMap(Map<String, String> searchMap) {
        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().startsWith("spec_")){
                searchMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
            }
        }
    }
}
