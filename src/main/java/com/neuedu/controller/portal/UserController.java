package com.neuedu.controller.portal;

import com.neuedu.comment.Const;
import com.neuedu.comment.ResponseCode;
import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ServerResponse login(HttpSession session,String username,String password){
        ServerResponse serverResponse = userService.login(username,password);
        if (serverResponse.isSuccess()){//保存登录状态
            session.setAttribute(Const.CURRENTUSER,serverResponse.getData());
        }
        return serverResponse;
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createServerResponseBySucess("退出成功");
    }
    /**
     *注册
     */
    @RequestMapping(value = "register.do")
    public ServerResponse register(UserInfo userInfo){
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

    /**
     * 忘记密码的重设密码
     */
    @RequestMapping(value = "forget_reset_password.do")
    public ServerResponse  forget_reset_password(String username,String passwordNew,String forgetToken){
        return userService.forget_reset_password(username,passwordNew,forgetToken);
    }
    /**
     * 检查用户名和邮箱是否有效
     */
    @RequestMapping(value = "check_valid.do")
    public ServerResponse check_valid(String str,String type){
       return userService.check_valid(str, type);
    }
    /**
     * 获取用户信息
     */
    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        Object o =session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo = (UserInfo) o;
            UserInfo responseUserInfo = new UserInfo();
            responseUserInfo.setId(userInfo.getId());
            responseUserInfo.setUsername(userInfo.getUsername());
            responseUserInfo.setEmail(userInfo.getEmail());
            responseUserInfo.setCreateTime(userInfo.getCreateTime());
            responseUserInfo.setUpdateTime(userInfo.getUpdateTime());
            return ServerResponse.createServerResponseBySucess(null,responseUserInfo);
        }
        //用户未登录
        return ServerResponse.createServerResponseByERROR(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }
    /**
     * 获取用户详细信息
     */
    @RequestMapping(value = "get_information.do")
    public ServerResponse get_information(HttpSession session){
        Object o =session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo = (UserInfo) o;
            return ServerResponse.createServerResponseBySucess(null,userInfo);
        }
        //用户未登录
        return ServerResponse.createServerResponseByERROR(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }

    /**
     *登录状态下修改密码
     */
    @RequestMapping(value = "reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew){
     Object o = session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo = (UserInfo) o;
            return userService.reset_password(userInfo,passwordOld,passwordNew);
        }
     return ServerResponse.createServerResponseByERROR(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }

    /**
     *登录状态下修改个人信息
     */
    @RequestMapping(value = "update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user){
       UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
       if (userInfo == null){
           return ServerResponse.createServerResponseByERROR(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
       }
       user.setId(userInfo.getId());
       ServerResponse serverResponse = userService.update_information(user);
       if (serverResponse.isSuccess()){
           //更新session用户信息
           UserInfo userInfo1 = userService.findUserInfoByUserId(userInfo.getId());
           session.setAttribute(Const.CURRENTUSER,userInfo1);
       }
        return serverResponse;
    }
}
