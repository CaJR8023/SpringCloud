package com.cajr.springcloud.service;

import com.cajr.springcloud.vo.Users;

/**
 * @Author CAJR
 * @create 2019/9/5 18:04
 */
public interface UserService {
    public Users getOneByName(String name);
}
