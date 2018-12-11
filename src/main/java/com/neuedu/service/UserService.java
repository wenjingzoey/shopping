package com.neuedu.service;

import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;

import javax.servlet.http.HttpSession;

public interface UserService {
    /**
     * 登录
     */
    public ServerResponse login(String username,String password);
    /**
     * 注册接口
     */
    public ServerResponse register(UserInfo userInfo);

    /**
     *根据用户名查找密保问题
     */
    public ServerResponse forget_get_question(String username);
/**
 * 提交密保问题
 */
    public ServerResponse forget_check_answer(String username,String question,String answer);
/**
 * 检查用户名和邮箱是否有效
 */
    public ServerResponse check_valid(String str,String type);

    /**
     *忘记密码的重设密码
     */
   public ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken);

    /**
     * 登录状态下修改密码
     */
    public ServerResponse reset_password(UserInfo user,String passwordOld, String passwordNew);

    public ServerResponse update_information(UserInfo user);
    public UserInfo findUserInfoByUserId(Integer userId);
}
