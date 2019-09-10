package com.cajr.springcloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author CAJR
 * @create 2019/9/10 12:09
 */
@Controller
public class ForePageController {
    @GetMapping("/index")
    public String index(){return "index";}

    @GetMapping("/article")
    public String article(){return "article";}

    @GetMapping("/list")
    public String list(){return "list";}

    @GetMapping("/about")
    public String about(){return "about";}

}
