package com.cajr.springcloud.mapper;

import com.cajr.springcloud.vo.Users;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 13:01
 */

public interface UsersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    List<Long> findAllId();

    List<Users> findAll();

    List<Users> findSectionUsers(List<Long> userIds);

}