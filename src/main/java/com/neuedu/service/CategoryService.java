package com.neuedu.service;

import com.neuedu.comment.ServerResponse;

public interface CategoryService {
    /**
     * 获取品类子节点(平级)
     */
    public ServerResponse get_category(Integer categoryId);

    /**
     *增加节点
     */
    public ServerResponse add_category(Integer parentId,String categoryName);


    public ServerResponse set_category_name(Integer categoryId,String categoryName);
}
