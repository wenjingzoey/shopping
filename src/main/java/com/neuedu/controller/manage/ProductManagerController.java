package com.neuedu.controller.manage;

import com.neuedu.comment.Const;
import com.neuedu.comment.ResponseCode;
import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product/")
public class ProductManagerController {
    @Autowired
    ProductService productService;
    /**
     *新增OR更新产品
     */
    @RequestMapping(value = "save.do")
    public ServerResponse saveOrUpdate(HttpSession session,Product product){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
     return productService.saveOrUpdate(product);
    }

    /**
     * 产品上下架
     */
    @RequestMapping(value = "set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session, Integer productId,Integer status){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return productService.set_sale_status(productId,status);
    }
    /**
     * 查看商品详情
     */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(HttpSession session, Integer productId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return productService.detail(productId);
    }

    /**
     * 产品list
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return productService.list(pageNum,pageSize);
    }
    /**
     * 产品搜索
     */
    @RequestMapping(value = "search.do")
    public ServerResponse search(HttpSession session,
                                 @RequestParam(value = "productId",required = false)Integer productId,
                                 @RequestParam(value = "productName",required = false)String productName,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        //判断用户权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NO_PRIVILEGE.getStatus(),ResponseCode.NO_PRIVILEGE.getMsg());
        }
        return productService.search(productId,productName,pageNum,pageSize);
    }
}
