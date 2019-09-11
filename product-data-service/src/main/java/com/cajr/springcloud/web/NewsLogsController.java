package com.cajr.springcloud.web;

import com.cajr.springcloud.service.NewsLogsService;
import com.cajr.springcloud.vo.Newslogs;
import com.cajr.springcloud.vo.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author CAJR
 * @create 2019/9/10 16:27
 */
@RestController
@RequestMapping("/news_logs")
public class NewsLogsController {

    @Resource
    NewsLogsService newsLogsService;

    @PostMapping("/")
    public Result add(@RequestBody Newslogs newslogs){
        return Result.success(this.newsLogsService.add(newslogs));
    }

    @GetMapping("/hot")
    public Result getHotNewsIds(){
        return Result.success(this.newsLogsService.hotNewsIdList());
    }
}
