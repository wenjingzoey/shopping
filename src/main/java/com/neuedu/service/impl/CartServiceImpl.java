package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.comment.Const;
import com.neuedu.comment.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.CartService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    /**
     *购物车添加商品
     */
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //step:1 非空校验
        if (productId == null || count == null){
            return ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(userId);
        if (product == null){
            return ServerResponse.createServerResponseByERROR("要添加的商品不存在");
        }
        //step:2 根据product和userId查询购物信息
        Cart cart =cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart == null){
            //添加
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckEnum.CART_CHECK.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVOLimit(userId);
        return  ServerResponse.createServerResponseBySucess(null,cartVO);
    }


    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO = new CartVO();
        //step:1 根据userId查询购物信息 --》List<Cart>
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //step:2 List<Cart>--》List<CartProductVO>
        List<CartProductVO> cartProductVOList = new ArrayList<>();
       //购物车总价格
        BigDecimal carttotalprice=new BigDecimal("0");
        if (cartList != null&&cartList.size()>0){
            for (Cart cart:cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductChecked(cart.getChecked());
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product!=null){
                 cartProductVO.setProductId(cart.getProductId());
                 cartProductVO.setProductMainImage(product.getMainImage());
                 cartProductVO.setProductName(product.getName());
                 cartProductVO.setProductPrice(product.getPrice());
                 cartProductVO.setProductStatus(product.getStatus());
                 cartProductVO.setProductStock(product.getStock());
                 cartProductVO.setProductSubtitle(product.getSubtitle());
                 int stock = product.getStock();
                 int limitProductCount=0;
                 if (stock>cart.getQuantity()){
                    limitProductCount = cart.getQuantity();
                    cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                 }else {//商品库存不足
                   limitProductCount=stock;
                   //更新购物车商品数量
                     Cart cart1 = new Cart();
                     cart1.setId(cart.getId());
                     cart1.setQuantity(stock);
                     cart1.setProductId(cart.getProductId());
                     cart1.setChecked(cart1.getChecked());
                     cart1.setUserId(userId);
                     cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                 }
                  cartProductVO.setQuantity(limitProductCount);
                 cartProductVO.setGproductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));
                }
                if (cartProductVO.getProductChecked() == Const.CartCheckEnum.CART_CHECK.getCode()) {
                    carttotalprice = BigDecimalUtils.add(carttotalprice.doubleValue(), cartProductVO.getGproductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);
        //step:3 计算总价格
        cartVO.setCarttotalprice(carttotalprice);
        //step:4 判断购物车是否全选
        int count = cartMapper.isCheckedAll(userId);
        if (count>0){
            cartVO.setIsallchecked(false);
        }else {
            cartVO.setIsallchecked(true);
        }
        //step:5 返回结果
        return cartVO;
    }


    /**
     * 购物车List列表
     */
    @Override
    public ServerResponse list(Integer userId) {
       CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySucess(null,cartVO);
    }
    /**
     *     更新购物车某个产品数量
     */
    @Override
    public ServerResponse update(Integer userId,Integer productId, Integer count) {
        //step:1参数校验
        if (productId == null || count == null){
            return ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        //step:2 查询购物车中的商品
        Cart cart =cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!= null){
            //step:3更新数量
          cart.setQuantity(count);
          cartMapper.updateByPrimaryKey(cart);
        }
        //step:返回cartvo
        return ServerResponse.createServerResponseBySucess(null,getCartVOLimit(userId));
    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //step:1非空校验
        if (productIds == null || productIds.equals("")){
            return ServerResponse.createServerResponseByERROR("参数不能为空");
        }
        //step:2productIds--》List<Integer>
        List<Integer> productIdList = Lists.newArrayList();
        String[] productIdsArr= productIds.split(",");
        if (productIdsArr!=null && productIdsArr.length>0){
            for (String productIdstr:productIdsArr) {
                Integer productId = Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        //step:3调用dao
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        //step:4非空校验
        return ServerResponse.createServerResponseBySucess(null,getCartVOLimit(userId));
    }
    /**
     * 购物车选中某个商品
     */
    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer checked) {
//        //step:1非空校验
//        if (productId == null){
//            return ServerResponse.createServerResponseByERROR("参数不能为空");
//        }
        Product product = productMapper.selectByPrimaryKey(userId);
        if (product == null){
            return ServerResponse.createServerResponseByERROR("要操作的商品不存在");
        }
        //step:2调用dao
        cartMapper.selectOrUpselectProduct(userId,productId,checked);
        //step:3返回接口
        return ServerResponse.createServerResponseBySucess(null,getCartVOLimit(userId));
    }

    /**
     * 统计用户购物车中的产品数量
     * @param userId
     * @return
     */
    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
       int quantity = cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySucess(null,quantity);
    }
}