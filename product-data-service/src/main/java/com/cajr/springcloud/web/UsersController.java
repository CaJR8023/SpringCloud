package com.cajr.springcloud.web;

import com.cajr.springcloud.service.UserService;
import com.cajr.springcloud.vo.Users;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author CAJR
 * @create 2019/9/11 20:44
 */
@RestController
@RequestMapping("/user")
public class UsersController {
    @Resource
    UserService userService;

    @GetMapping("/name")
    public Users getUserByName(@RequestParam("name") String userName){
        return this.userService.getOneByName(userName);
    }
}
