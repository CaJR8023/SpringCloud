package com.cajr.springcloud.web;

import com.cajr.springcloud.common.content.CustomizedHashMap;
import com.cajr.springcloud.service.NewsModulesService;
import com.cajr.springcloud.util.NewsImportUtil;
import com.cajr.springcloud.vo.Newsmodules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/9 15:44
 */
@RestController
@RequestMapping("/news_modules")
public class NewsModulesController {
    @Autowired
    NewsModulesService newsModulesService;

    @GetMapping("/")
    public List<Newsmodules> newsModulesList() throws IOException {
        return this.newsModulesService.list();
    }
    @GetMapping("/moduleIds")
    public String newsModulesIdList() throws IOException {
        List<Newsmodules> newsmodulesList =  this.newsModulesService.list();
        CustomizedHashMap<Integer,String> customizedHashMap = new CustomizedHashMap<>();
        for (Newsmodules newsmodules : newsmodulesList) {
            customizedHashMap.put(newsmodules.getId(),"");
        }
//        List<Newsmodules> newsmodules = new ArrayList<>();
//        for (Newsmodules newsmodules1: newsmodulesList) {
//            newsmodules1.setName("{}");
//            newsmodules.add(newsmodules1);
//        }
//        return newsmodules;
        return customizedHashMap.toString();
    }
}
