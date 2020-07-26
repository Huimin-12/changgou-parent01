package com.changgou.controller;


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
