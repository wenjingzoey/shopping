package com.neuedu.service;

import com.neuedu.comment.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface ProductService {
    /**
     *新增OR更新产品
     */
    public ServerResponse saveOrUpdate(Product product);

    /**
     * 产品上下架
     * @param productId
     * @param status 状态
     * @return
     */
    public ServerResponse set_sale_status(Integer productId, Integer status);


    /**
     * 商品详情
     * @param productId
     * @return
     */
    public ServerResponse detail(Integer productId);
    /**
     * 产品list
     */
    public ServerResponse list(Integer pageNum,Integer pageSize);
    /**
     * 后台搜索商品
     */
   public ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);
/**
 * 图片上传
 */
public ServerResponse upload(MultipartFile file,String path);
    /**
     * 前台商品详情
     */
public ServerResponse detail_portal(Integer productId);
    /**
     * 前台产品搜索及动态排序List
     */
    public ServerResponse list_portal(Integer categoryId,String keyword,Integer pageNum,Integer pageSize,String orderBy);
}
