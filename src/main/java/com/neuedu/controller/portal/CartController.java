package com.neuedu.controller.portal;

import com.neuedu.comment.Const;
import com.neuedu.comment.ResponseCode;
import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/cart/")
public class CartController {
    @Autowired
    CartService cartService;
    /**
     *购物车添加商品
     */
    @RequestMapping(value = "add.do")
    public ServerResponse add(HttpSession session,Integer productId, Integer count){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
    return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     * 购物车List列表
     * @param session
     * @return
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.list(userInfo.getId());
    }


    /**
     * 更新购物车某个产品数量
     */
    @RequestMapping(value = "update.do")
    public ServerResponse update(HttpSession session,Integer productId, Integer count){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.update(userInfo.getId(),productId,count);
    }

    /**
     * 移除购物车某个产品
     */

    @RequestMapping(value = "delete_product.do")
    public ServerResponse delete_product(HttpSession session,String productIds){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.delete_product(userInfo.getId(),productIds);
    }
/**
 * 购物车选中某个商品
 */
@RequestMapping(value = "select.do")
public ServerResponse select(HttpSession session,Integer productId){
    UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
    if (userInfo == null){
        return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
    }
    return cartService.select(userInfo.getId(),productId,Const.CartCheckEnum.CART_CHECK.getCode());
}

    /**
     * 取消购物车选中某个商品
     */
    @RequestMapping(value = "un_select.do")
    public ServerResponse un_select(HttpSession session,Integer productId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckEnum.CART_ON_CHECK.getCode());
    }

    /**
     * 购物车全选
     */
    @RequestMapping(value = "select_all.do")
    public ServerResponse select_all(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckEnum.CART_CHECK.getCode());
    }
    /**
     * 取消购物车全选
     */
    @RequestMapping(value = "un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckEnum.CART_ON_CHECK.getCode());
    }

    /**
     * 查询在购物车里的产品数量
     */
    @RequestMapping(value = "get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.createServerResponseByERROR(ResponseCode.NEED_LOGIN.getStatus(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return cartService.get_cart_product_count(userInfo.getId());
    }

}
