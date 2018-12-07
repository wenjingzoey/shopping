package com.neuedu.service.impl;

import com.neuedu.comment.Const;
import com.neuedu.comment.ResponseCode;
import com.neuedu.comment.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse login(String username, String password) {
        //step:1非空校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByERROR("用户名不能为空");
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByERROR("密码不能为空");
        }
        //step:2 检查username是否存在
//       int result = userInfoMapper.checkUsername(username);
//       if (result <=0){
//         return ServerResponse.createServerResponseByERROR("用户名不存在");
//       }
        ServerResponse serverResponse = check_valid(username,Const.USERNAME);
        if (serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NOT_EXISTS_USERNAME.getStatus(),ResponseCode.NOT_EXISTS_USERNAME.getMsg());
        }
        //step:3 根据用户名和密码进行查询
        UserInfo userInfo = userInfoMapper.selectUserByUsernameAndPassword(username,MD5Utils.getMD5Code(password));
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR("密码有误");
        }
        userInfo.setPassword("");
        //step:4 处理结果并返回
        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //stemp:1 参数非空检验
        if (userInfo == null){
           return ServerResponse.createServerResponseByERROR(ResponseCode.PARAM_EMPTY.getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //stemp:2 校验用户名
//        int result = userInfoMapper.checkUsername(userInfo.getUsername());
//        if (result >0){//用户名已存在
//            return ServerResponse.createServerResponseByERROR(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
//        }
        String username = userInfo.getUsername();
        ServerResponse serverResponse = check_valid(username,Const.USERNAME);
        if (!serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
        }
//        //stemp:3 校验邮箱
//        int result_email = userInfoMapper.checkEmail(userInfo.getEmail());
//        if (result_email >0){//邮箱已存在
//            return ServerResponse.createServerResponseByERROR(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
//        }
        String email = userInfo.getEmail();
        ServerResponse email_serverResponse = check_valid(email,Const.EMAIL);
        if (!email_serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
        }
        //stemp:4 注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int count = userInfoMapper.insert(userInfo);
        if (count>0){
            return ServerResponse.createServerResponseBySucess("注册成功");
        }
        //stemp:5 返回结束
        return ServerResponse.createServerResponseByERROR("注册失败");
    }

    /**
     * 检查用户名和邮箱是否有效
     */
    @Override
    public ServerResponse check_valid(String str, String type) {
        //step1:参数非空校验
        if (StringUtils.isBlank(str) || StringUtils.isBlank(type)){
            ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        //step2:判断用户名和邮箱是否存在
        if (type.equals(Const.USERNAME)){
            int username_result=userInfoMapper.checkUsername(str);
            if (username_result>0){
                return ServerResponse.createServerResponseByERROR(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());

            }
            return ServerResponse.createServerResponseBySucess("成功");
        }else if (type.equals(Const.EMAIL)){
            int email_result=userInfoMapper.checkEmail(str);
            if (email_result>0){
                return ServerResponse.createServerResponseByERROR(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());

            }
            return ServerResponse.createServerResponseBySucess("成功");
        }
        //step3返回结果
        return ServerResponse.createServerResponseByERROR("type参数传递有误");
    }



    /**
     * 根据用户名查找密保问题
     */
    @Override
    public ServerResponse forget_get_question(String username) {
        //stemp1: 参数检验
        if (StringUtils.isBlank(username)){
                return ServerResponse.createServerResponseByERROR("用户名不能为空");
        }
        //stemp2:校验username
        int result = userInfoMapper.checkUsername(username);
        if (result == 0){
            return ServerResponse.createServerResponseByERROR("用户名不存在，请重新输入");
        }
        //stemp3:查找密保问题
        String question = userInfoMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByERROR("密保问题为空");
        }
        return ServerResponse.createServerResponseBySucess(question);
    }


    /**
     * 根据用户名和密保问题以及密保答案进行查询
     */
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //step1:参数校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByERROR("用户名不能为空");
        }
        if (StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByERROR("密保问题不能为空");
        }
        if (StringUtils.isBlank(answer)){
            return ServerResponse.createServerResponseByERROR("密保答案不能为空");
        }
        //step2:根据username,question,answer查询
       int result = userInfoMapper.selectByUsernameAndPQuestionAndAnswer(username,question,answer);
        if (result == 0){
            return ServerResponse.createServerResponseByERROR("密保答案错误，请重新输入");
        }
        //step3:服务端生成一个token保存并将token返回给客户端  UUID生成的是唯一的字符串
        String forgetToken=UUID.randomUUID().toString();
        //guava cache 做一个guava缓存
        TokenCache.set(username,forgetToken);
        return ServerResponse.createServerResponseBySucess(null,forgetToken);
}


    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken) {
        //step1:非空校验
        if (StringUtils.isBlank(username)){
            ServerResponse.createServerResponseByERROR("用户名不能为空");
        }
        if (StringUtils.isBlank(passwordNew)){
            ServerResponse.createServerResponseByERROR("密码不能为空");
        }
        if (StringUtils.isBlank(forgetToken)){
            ServerResponse.createServerResponseByERROR("token不能为空");
        }
        //step2:token校验
        String token = TokenCache.get(username);
        if (token == null){
            return ServerResponse.createServerResponseByERROR("token过期");
        }
        if (!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByERROR("无效的token");
        }
        //step3:修改密码
        int result = userInfoMapper.updatePasswordByUsername(username,MD5Utils.getMD5Code(passwordNew));
        if (result>0){
            return ServerResponse.createServerResponseBySucess("密码重置成功");
        }
        return ServerResponse.createServerResponseBySucess("密码重置失败");
    }
    /**
     *登录状态下修改密码
     */
    @Override
    public ServerResponse reset_password(UserInfo user, String passwordOld, String passwordNew) {
         //step1:非空校验
        if (StringUtils.isBlank(passwordOld) ||StringUtils.isBlank(passwordNew)){
            return ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        //step2:校验旧密码是否正确
        UserInfo userInfoOld = userInfoMapper.selectUserByUsernameAndPassword(user.getUsername(),MD5Utils.getMD5Code(passwordOld));
        if (userInfoOld == null){
            return ServerResponse.createServerResponseByERROR("旧密码错误，请重新输入");
        }
        //step3:修改密码
        int count = userInfoMapper.updatePasswordByUsername(user.getUsername(),MD5Utils.getMD5Code(passwordNew));
        //step4:返回结果
        if (count<=0){
            return ServerResponse.createServerResponseByERROR("密码修改失败");
        }
        return ServerResponse.createServerResponseBySucess("密码修改成功");
    }


    /**
     * 登录状态下修改密码
     * @param user
     * @return
     */
    @Override
    public ServerResponse update_information(UserInfo user) {
        //step1:参数校验
        if (user == null){
            return ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        //step2:更新用户信息
        int result = userInfoMapper.updateUserBySelectActive(user);
        if (result>0){
            return ServerResponse.createServerResponseByERROR("修改成功");
        }
        return ServerResponse.createServerResponseByERROR("修改失败");
    }
}