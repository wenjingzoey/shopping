package com.neuedu.controller.manage;

import com.neuedu.comment.Const;
import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台用户控制器
 */
@RestController
@RequestMapping(value = "/manage/user/")
public class UserManagerController {
    @Autowired
    UserService userService;
    /**
     * 管理员登录
     */
    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session, String username, String password){
        ServerResponse serverResponse = userService.login(username,password);
        if (serverResponse.isSuccess()){//保存登录状态
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
               return ServerResponse.createServerResponseByERROR("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }
}
