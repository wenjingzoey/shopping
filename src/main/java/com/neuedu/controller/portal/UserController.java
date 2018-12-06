package com.neuedu.controller.portal;

import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {
    @Autowired
    UserService userService;

    /**
     *根据用户名和密码进行登录
     */
    @RequestMapping(value = "login.do")
    public ServerResponse login(String username,String password){
     return userService.login(username,password);
    }

    /**
     *注册
     */
    @RequestMapping(value = "register.do")
    public ServerResponse register(HttpSession session, UserInfo userInfo){
        return userService.register(userInfo);
    }
    /**
     * 根据用户名查找密保问题
     */
    @RequestMapping(value = "forget_get_question.do")
    public ServerResponse forget_get_question(String username){
        return userService.forget_get_question(username);
    }
    /**
     * 根据用户名和密保问题以及密保答案进行查询
     */
    @RequestMapping(value = "forget_check_answer.do")
   public ServerResponse forget_check_answer(String username,String question,String answer){
        return userService.forget_check_answer(username,question,answer);
   }
}
