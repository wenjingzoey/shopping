package com.neuedu.service.impl;

import com.neuedu.comment.Const;
import com.neuedu.comment.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //stemp:1 参数非空检验
        if (userInfo == null){
           return ServerResponse.createServerResponseByERROR("参数必需");
        }
        //stemp:2 校验用户名
        int result = userInfoMapper.checkUsername(userInfo.getUsername());
        if (result >0){
            return ServerResponse.createServerResponseByERROR("用户名已存在");
        }
        //stemp:3 校验邮箱
        int result_email = userInfoMapper.checkEmail(userInfo.getEmail());
        if (result_email >0){
            return ServerResponse.createServerResponseByERROR("邮箱已存在");
        }
        //stemp:4 注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        int count = userInfoMapper.insert(userInfo);
        if (count>0){
            return ServerResponse.createServerResponseBySucess("注册成功");
        }
        //stemp:5 返回结束
        return ServerResponse.createServerResponseByERROR("注册失败");
    }

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
        return null;

}
    @Override
    public ServerResponse login(String username, String password) {
        //step:1非空校验
        if (StringUtils.isBlank(username)){
           return ServerResponse.createServerResponseByERROR("用户名不能为空");
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByERROR("密码不能为空");
        }
/*        if (username == null || username.equals(" ")){

       }
*/
        //step:2 检查username是否存在
       int result = userInfoMapper.checkUsername(username);
       if (result <=0){
         return ServerResponse.createServerResponseByERROR("用户名不存在");
       }
        //step:3 根据用户名和密码进行查询
        UserInfo userInfo = userInfoMapper.selectUserByUsernameAndPassword(username,password);
       if (userInfo == null){
           ServerResponse.createServerResponseByERROR("密码有误");
       }
       userInfo.setPassword("");
        //step:4 处理结果并返回
        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }
}