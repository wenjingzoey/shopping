package com.neuedu.service;

import com.neuedu.comment.ServerResponse;

public interface CartService {
    /**
     *购物车添加商品
     */
    public ServerResponse add(String productId, Integer count);
}
