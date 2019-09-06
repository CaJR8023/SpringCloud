package com.cajr.springcloud.service;

import com.cajr.springcloud.vo.Newsmodules;

/**
 * @Author CAJR
 * @create 2019/9/5 18:05
 */
public interface NewsModulesService {
    Newsmodules selectByName(String name);

    int add(Newsmodules newsmodules);
}
