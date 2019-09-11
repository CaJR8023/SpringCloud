package com.cajr.springcloud.service.impl;

import com.cajr.springcloud.mapper.UsersMapper;
import com.cajr.springcloud.service.UserService;
import com.cajr.springcloud.vo.Users;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author CAJR
 * @create 2019/9/11 20:49
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UsersMapper usersMapper;

    @Override
    public Users getOneByName(String name) {
        return this.usersMapper.findOneByName(name);
    }
}
