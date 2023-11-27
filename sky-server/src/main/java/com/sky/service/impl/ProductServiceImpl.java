package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.ProductDTO;
import com.sky.entity.Product;
import com.sky.exception.GoodsNullException;
import com.sky.exception.ObjectNullException;
import com.sky.exception.RepeatException;
import com.sky.mapper.ProductMapper;
import com.sky.service.ProductService;
import com.sky.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    /**
     * 查看所有产品
     * @return
     */
    @Override
    public List<ProductVO> showAll() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus,Product.ALIVE);
        List<Product> products = baseMapper.selectList(wrapper);
        List<ProductVO> productVOS = products.stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            return vo;
        }).collect(Collectors.toList());
        return productVOS;
    }

    /**
     * 管理员查看所有产品,包括已被删除的
     * @return
     */
    @Override
    public List<ProductVO> showAllAdmin() {
        List<Product> products = baseMapper.selectList(null);
        List<ProductVO> productVOS = products.stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            return vo;
        }).collect(Collectors.toList());
        return productVOS;
    }

    /**
     * 按id查找
     * @param id
     * @return
     */
    @Override
    public ProductVO showOneByID(Long id) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getId,id);
        wrapper.eq(Product::getStatus,Product.ALIVE);
        Product product = baseMapper.selectOne(wrapper);
        if (Objects.isNull(product)){
            throw new ObjectNullException(MessageConstant.PRODUCT_NULL);
        }
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }

    /**
     * 管理员按id查找,包括已删除的
     * @param id
     * @return
     */
    @Override
    public ProductVO showOneByIDAdmin(Long id) {
        Product product = baseMapper.selectById(id);
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }

    /**
     * 添加新产品
     * @param productDTO
     * @param token
     * @return
     */
    @Override
    public int addProduct(ProductDTO productDTO, String token) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        //检查产品名称是否相同，相同则无法添加
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getName,product.getName());
        if (!Objects.isNull(baseMapper.selectOne(wrapper))){
            throw new RepeatException(MessageConstant.PRODUCT_HAS_BEEN_CREATED);
        }

        return baseMapper.insert(product);
    }

    /**
     * 修改产品信息
     * @param product
     * @param token
     * @return
     */
    @Override
    public int changeProduct(Product product, String token) {
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();

        if (StringUtils.hasText(product.getName())) {
            wrapper.set(Product::getName, product.getName());
        }

        if (product.getPrice() != null) {
            wrapper.set(Product::getPrice, product.getPrice());
        }

        if (StringUtils.hasText(product.getDescription())) {
            wrapper.set(Product::getDescription, product.getDescription());
        }

        if (product.getGoods() != null) {
            wrapper.set(Product::getGoods, product.getGoods());
        }

        wrapper.eq(Product::getId, product.getId());

        return baseMapper.update(product, wrapper);
    }

    /**
     * 逻辑删除
     * @param ids
     * @param token
     * @return
     */
    @Override
    public int deleteByIDs(Long[] ids, String token) {
        List<Long> idList = Arrays.asList(ids);
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Product::getId, idList)
                .set(Product::getStatus, Product.DISABLE);
        return baseMapper.update(null, wrapper);
    }

    /**
     * 恢复逻辑删除
     * @param ids
     * @param token
     * @return
     */
    @Override
    public int recoverByIDS(Long[] ids, String token) {
        List<Long> idList = Arrays.asList(ids);
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Product::getId, idList)
                .set(Product::getStatus, Product.ALIVE);
        return baseMapper.update(null, wrapper);
    }

    /**
     * 批量查询商品
     * @param ids
     * @return
     */
    @Override
    public List<Product> selectProductByIDs(Long[] ids) {
        List<Long> selectIDs = Arrays.asList(ids);
        LambdaQueryWrapper<Product> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(Product::getId, selectIDs);
        List<Product> ordersList = baseMapper.selectList(wrapper1);
        return ordersList;
    }

    /**
     * 计算订单总金额
     * @param ids
     * @return
     */
    @Override
    public AtomicInteger countTotalPriceByIDs(Long[] ids) {
        List<Product> products = selectProductByIDs(ids);
        AtomicInteger totalAmount = new AtomicInteger();
        products.forEach((product) -> {
            totalAmount.addAndGet(product.getPrice().intValue());
        });
        return totalAmount;
    }

    /**
     * 商品库存减一
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public void productGoodsMinus(Long[] ids) {
        List<Long> selectIDs = Arrays.asList(ids);
        LambdaUpdateWrapper<Product> wrapper = null;

        // 获取所有商品的数量
        List<Product> products = baseMapper.selectList(new LambdaQueryWrapper<Product>().in(Product::getId, selectIDs));

        for (Product product : products) {
            wrapper = new LambdaUpdateWrapper<>();
            Long goods = product.getGoods() - 1;
            if (goods < 0) {
                throw new GoodsNullException(MessageConstant.GOODS_NULL);
            }
            wrapper.set(Product::getGoods, goods).eq(Product::getId, product.getId());
            baseMapper.update(null, wrapper);
        }

    }

    @Override
    public void productGoodsRecovery(Long[] ids) {
        List<Long> selectIDs = Arrays.asList(ids);
        LambdaUpdateWrapper<Product> wrapper = null;

        // 获取所有商品的数量
        List<Product> products = baseMapper.selectList(new LambdaQueryWrapper<Product>().in(Product::getId, selectIDs));

        for (Product product : products) {
            wrapper = new LambdaUpdateWrapper<>();
            Long goods = product.getGoods() + 1;
            wrapper.set(Product::getGoods, goods).eq(Product::getId, product.getId());
            baseMapper.update(null, wrapper);
        }
    }


}
