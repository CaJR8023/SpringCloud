package com.cajr.springcloud.client;

import com.cajr.springcloud.client.fallback.ProductClientFeignHystrix;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.Recommendations;
import com.cajr.springcloud.vo.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "PRODUCT-DATA-SERVICE",fallbackFactory = ProductClientFeignHystrix.class)
public interface ClientFeign {

    @GetMapping("/news/")
    public List<News> listNews();

    @GetMapping("/recommendations/")
    public List<Recommendations> listRecommendations(@RequestParam("userId") Long userId);

    @PostMapping("/news_logs/")
    public Result addUserNewsLogs(@RequestBody Newslogs newslogs);
}
