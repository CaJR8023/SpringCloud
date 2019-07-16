package com.cajr.springcloud.web;

import com.cajr.springcloud.pojo.Product;
import com.cajr.springcloud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RefreshScope
public class ProductController {
    @Autowired
    ProductService productService;

    @Value("${version}")
    String version;

    @RequestMapping("/products")
    public Object products(Model model){
        List<Product> ps=productService.listProducts();
        model.addAttribute("version",version);
        model.addAttribute("ps",ps);
        return "products";
    }
}
