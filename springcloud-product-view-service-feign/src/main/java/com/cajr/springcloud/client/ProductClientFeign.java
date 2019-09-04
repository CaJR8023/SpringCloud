package com.cajr.springcloud.client;

import com.cajr.springcloud.client.fallback.ProductClientFeignHystrix;
import com.cajr.springcloud.vo.News;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "PRODUCT-DATA-SERVICE",fallbackFactory = ProductClientFeignHystrix.class)
public interface ProductClientFeign {

    @GetMapping("/news")
    public List<News> listNews();
}
