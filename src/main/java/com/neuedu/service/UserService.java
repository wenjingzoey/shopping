package com.neuedu.service;

import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;

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
}
