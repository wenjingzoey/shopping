package com.neuedu.service;

import com.neuedu.comment.ServerResponse;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;

public interface CartService {
    /**
     *购物车添加商品
     */
    public ServerResponse add(Integer userId,Integer productId, Integer count);
    /**
     * 购物车List列表
     */
    public ServerResponse list(Integer userId);

    /**
     * 更新购物车某个产品数量
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse update(Integer userId,Integer productId, Integer count);


    /**
     * 移除购物车某个产品
     */
    public ServerResponse delete_product(Integer userId,String productIds);


    /**
     * 购物车选中某个商品
     */
    public ServerResponse select(Integer userId,Integer productId,Integer checked);

    /**
     * 查询在购物车里的产品数量
     */
    public ServerResponse get_cart_product_count(Integer userId);
}
