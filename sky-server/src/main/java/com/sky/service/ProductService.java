package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.PageDTO;
import com.sky.dto.ProductDTO;
import com.sky.entity.Product;
import com.sky.result.PageQuery;
import com.sky.vo.ProductVO;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface ProductService extends IService<Product> {

    PageDTO<ProductVO> showAllByPage(PageQuery query);

    PageDTO<ProductVO> showAllAdminByPage(PageQuery query);

    ProductVO showOneByID(Long id);

    ProductVO showOneByIDAdmin(Long id);

    int addProduct(ProductDTO productDTO, String token);

    int changeProduct(Product product, String token);

    int deleteByIDs(Long[] ids, String token);

    int recoverByIDS(Long[] ids, String token);

    List<Product> selectProductByIDs(Long[] ids);

    AtomicInteger countTotalPriceByIDs(Long[] ids);

    void productGoodsMinus(Long[] ids);

    void productGoodsRecovery(Long[] ids);

}
