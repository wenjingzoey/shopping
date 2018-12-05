package com.neuedu.controller;

import com.neuedu.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//往前端返回的是json数据
@RestController
@RequestMapping(value = "/portal/user")
 public class TestController{

    @RequestMapping(value = "/login.do")
    public UserInfo login(){
    UserInfo userInfo = new UserInfo();
    userInfo.setId(1);
    userInfo.setUsername("静");
    return  userInfo;
    }
}
