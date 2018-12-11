package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.comment.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    /**
     * 获取品类子节点(平级)
     */
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //step1:非空校验
        if (categoryId == null){
            return ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        //step2:根据categoryId查询类别
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ServerResponse.createServerResponseByERROR("查询类别不存在");
        }
        //step3:查询子类别
       List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        //step4:返回结束
        return ServerResponse.createServerResponseBySucess(null,categoryList);
    }

    /**
     * 添加节点
     * @param parentId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //step:1参数校验
        if (StringUtils.isBlank(categoryName)){
            return ServerResponse.createServerResponseByERROR("类别名称不能为空");
        }
        //step2:添加节点
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int result = categoryMapper.insert(category);
        if (result>0){
            return ServerResponse.createServerResponseBySucess("添加成功");
        }
        //step3:返回结果
        return ServerResponse.createServerResponseByERROR("添加失败");
    }

    /**
     * 修改节点
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //step:1参数非空交验
        if (categoryId == null || categoryId.equals(" ")){
            return ServerResponse.createServerResponseByERROR("类别id不能为空");
        }
        if (StringUtils.isBlank(categoryName)){
            return ServerResponse.createServerResponseByERROR("类别名称不能为空");
        }
        //step:2根据category进行查询
         Category category= categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ServerResponse.createServerResponseBySucess("要修改的类别不存在");
        }
        //step:3修改
        category.setName(categoryName);
       int result = categoryMapper.updateByPrimaryKey(category);
        if (result>0){
            return ServerResponse.createServerResponseBySucess("修改成功");
        }
        //step:4结果
        return ServerResponse.createServerResponseByERROR("修改失败");
    }
    /**
     * 获取当前分类id及递归子节点categoryId
     */
    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //step:1非空校验
        if (categoryId == null || categoryId.equals(" ")){
            return ServerResponse.createServerResponseByERROR("类别id不能为空");
        }
        //step:2查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet=findAllChildCategory(categorySet,categoryId);
        Set<Integer> integerSet = Sets.newHashSet();
        Iterator<Category> categoryIterator=  categorySet.iterator();
         while (categoryIterator.hasNext()){
             Category category = categoryIterator.next();
             integerSet.add(category.getId());
         }
        return ServerResponse.createServerResponseBySucess(null,integerSet);
    }

    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找categoryId下的子节点（平级）
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if (categoryList != null && categoryList.size() > 0) {
            for (Category category1 : categoryList) {
                findAllChildCategory(categorySet, category1.getId());
            }
        }
        return categorySet;
    }

}
