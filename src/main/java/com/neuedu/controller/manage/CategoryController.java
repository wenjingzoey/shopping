package com.neuedu.controller.manage;

import com.neuedu.comment.Const;
import com.neuedu.comment.ResponseCode;
import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category/")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    /**
     * 获取品类子节点(平级)
     */
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return categoryService.get_category(categoryId);
    }


    /**
     * 增加节点
     */
    @RequestMapping(value = "add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue = "0") Integer parentId,
                                       String categoryName) {

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return categoryService.add_category(parentId,categoryName);
    }

    /**
     * 修改节点
     */
    @RequestMapping(value = "set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,
                                            Integer categoryId,
                                            String categoryName) {

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return categoryService.add_category(categoryId,categoryName);
    }

}
