package com.cajr.springcloud.client.fallback;

import com.cajr.springcloud.client.ClientFeign;
import com.cajr.springcloud.vo.News;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.Recommendations;
import com.cajr.springcloud.vo.result.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductClientFeignHystrix implements ClientFeign {

    @Override
    public List<News> listNews() {
        List<News> result = new ArrayList<>();
        News news = new News();
        news.setId(0L);
        result.add(news);
        return result;
    }

    @Override
    public List<Recommendations> listRecommendations(Long userId) {
        return null;
    }

    @Override
    public Result addUserNewsLogs(Newslogs newslogs) {
        return null;
    }
}
