package com.cajr.springcloud.service;

import com.cajr.springcloud.client.ClientFeign;
import com.cajr.springcloud.vo.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsService {

    @Resource
    ClientFeign productClientFeign;

    public List<News> listNews(){
        return productClientFeign.listNews();
    }
}
