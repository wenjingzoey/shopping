package com.neuedu.controller.portal;

import com.neuedu.comment.ServerResponse;
import com.neuedu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/portal/product/")
public class ProductController {
    @Autowired
    ProductService productService;
    /**
     * 前台商品详情
     */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(Integer productId){
     return productService.detail_portal(productId);
    }

    /**
     * 前台产品搜索及动态排序List
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy 排序字段
     * @return
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                               @RequestParam(value = "keyword",required = false)String keyword,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy){
        return productService.list_portal(categoryId, keyword,pageNum,pageSize,orderBy);
    }
}
