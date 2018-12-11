package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.comment.Const;
import com.neuedu.comment.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.CategoryService;
import com.neuedu.service.ProductService;
import com.neuedu.utils.DateUtil;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryService categoryService;
    /**
     *新增OR更新产品
     */
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        //step:1非空校验
     if (product == null){
         return ServerResponse.createServerResponseByERROR("参数不能为空");
     }
       //step:2 设置商品主图sub_images --> 1.jpg  2.jpg
        String subImages=product.getSubImages();
     if (subImages != null && subImages.equals("")){
         String[] subImageArr = subImages.split(",");
         if (subImageArr.length>0){
             //设置产品主图
             product.setMainImage(subImageArr[0]);
         }
     }
        //step:3 商品 save or update
        if (product.getId() == null){
         //添加
          int result = productMapper.insert(product);
          if (result>0){
              return ServerResponse.createServerResponseBySucess("添加成功");
          }else {
              return ServerResponse.createServerResponseByERROR("添加失败");
          }
        }else {
        //更新
            int result = productMapper.updateByPrimaryKey(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess("更新成功");
            }else{
                return ServerResponse.createServerResponseByERROR("更新失败");
            }
        }


    }
    /**
     * 产品上下架
     */
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        //step:1 非空校验
        if (productId == null){
            return ServerResponse.createServerResponseByERROR("商品id参数不能为空");
        }
        if (status == null){
            return ServerResponse.createServerResponseByERROR("商品状态参数不能为空");
        }
        //step:2 更新商品状态
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
       int result = productMapper.updateProductSelective(product);
        //step:3 返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("更新成功");
        }else {
            return ServerResponse.createServerResponseByERROR("更新失败");
        }
    }

    @Override
    public ServerResponse detail(Integer productId) {
        //step:1参数非空校验
        if (productId == null){
            return ServerResponse.createServerResponseByERROR("商品id参数不能为空");
        }
        //step:2查询product
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createServerResponseByERROR("商品不存在");
        }
        //step:3product--->productDetailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        //step:4结果返回
        return ServerResponse.createServerResponseBySucess(null,productDetailVO);
    }
    /**
     * 产品list
     */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //step:1查询商品数据
        List<Product> productList = productMapper.selectAll();
        List<ProductListVO> productListVOList = new ArrayList<>();
        if (productList != null && productList.size()>0){
            for (Product product:productList) {
             ProductListVO productListVO = assembleProductListVO(product);
             productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }


    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetall());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category != null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            //默认根节点
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }
    /**
     * 后台搜索商品
     */
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
     PageHelper.startPage(pageNum,pageSize);
     if (productName!= null && productName.equals("")){
         productName="%"+productName+"%";
     }
    List<Product> productList = productMapper.findProductByProductIdAndProductName(productId,productName);
        List<ProductListVO> productListVOList = new ArrayList<>();
        if (productList != null && productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
     return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file,String path) {
        if (file == null){
            return ServerResponse.createServerResponseByERROR();
        }
        //step:1获取图片名称
        String orignalFileName = file.getOriginalFilename();
        //step:2获取图片的扩展名
        String exName = orignalFileName.substring(orignalFileName.lastIndexOf("."));//.jsp
      //step:3为图片生成新的唯一的名字
        String newFileName= UUID.randomUUID().toString()+exName;
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdir();
        }
        File file1 = new File(path,newFileName);
        try {
            file.transferTo(file1);
            //上传到图片服务器
            //......
            Map<String,String> map =Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            return ServerResponse.createServerResponseBySucess(null,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 前台商品详情
     */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        //step:1参数非空校验
        if (productId == null){
            return ServerResponse.createServerResponseByERROR("商品id参数不能为空");
        }
        //step2:查询product
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createServerResponseByERROR("商品不存在");
        }
        //step:3校验商品状态
        if (product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
            return ServerResponse.createServerResponseByERROR("商品已下架或删除");
        }
        //step:4获取productDetailVO
       ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        //step:5返回结果
        return ServerResponse.createServerResponseBySucess(null,productDetailVO);
    }
/**
 * 前台产品搜索及动态排序List
 */
    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step:1参数校验   categoryId和keyword不能同时为空
        if (categoryId == null && (keyword == null || keyword.equals(""))){
            return ServerResponse.createServerResponseByERROR("参数错误");
        }
        //step:2  categoryId
        Set<Integer> integerSet = Sets.newHashSet();
        if (categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && (keyword == null || keyword.equals(""))){
                //说明没有数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList = new ArrayList<>();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySucess(null,pageInfo);
            }
            ServerResponse serverResponse = categoryService.get_deep_category(categoryId);
            if (serverResponse.isSuccess()){
                integerSet= (Set<Integer>) serverResponse.getData();
            }
        }
        //step:3 keyword
        if (keyword!=null&&keyword.equals("")){
            keyword ="%"+keyword+"%";
        }
        if (orderBy.equals("")) {
            PageHelper.startPage(pageNum,pageSize);
        }else {
            String[] orderByArr = orderBy.split("_");
            if (orderByArr.length>1){
                PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else {
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        //step:4 List<Product> -->List<ProductListVO>
        List<Product> productList = productMapper.searchProduct(integerSet,keyword);
        List<ProductListVO> productListVOList = Lists.newArrayList();
   if (productList!=null &&productList.size()>0){
       for (Product product:productList) {
           ProductListVO productListVO = assembleProductListVO(product);
           productListVOList.add(productListVO);
       }
   }
        //step:5分页
        PageInfo pageInfo = new PageInfo(productListVOList);
        //step:返回结果
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
}
