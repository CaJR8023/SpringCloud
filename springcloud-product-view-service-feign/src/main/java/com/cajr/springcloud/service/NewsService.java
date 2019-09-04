package com.cajr.springcloud.service;

import com.cajr.springcloud.client.ProductClientFeign;
import com.cajr.springcloud.vo.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsService {

    @Resource
    ProductClientFeign productClientFeign;

    public List<News> listNews(){
        return productClientFeign.listProducts();
    }
}
