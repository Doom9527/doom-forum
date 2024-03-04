package com.sky.controller.web;

import com.sky.dto.PageDTO;
import com.sky.dto.ProductDTO;
import com.sky.entity.Product;
import com.sky.result.PageQuery;
import com.sky.result.Result;
import com.sky.service.ProductService;
import com.sky.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 查看所有产品
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<PageDTO<ProductVO>> showAllProductByPage(@Valid PageQuery query) {
        PageDTO<ProductVO> productVOs = productService.showAllByPage(query);
        return Result.success(productVOs);
    }

    /**
     * 程序员查看所有产品，包括删除的
     * @return
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<PageDTO<ProductVO>> showAllProductAdminByPage(PageQuery query) {
        PageDTO<ProductVO> productVOs = productService.showAllAdminByPage(query);
        return Result.success(productVOs);
    }

    /**
     * 按id查找
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<ProductVO> showOneProductByID(@PathVariable Long id) {
        ProductVO productVO = productService.showOneByID(id);
        return Result.success(productVO);
    }

    /**
     * 程序员按id查找，包括已被删除的
     * @param id
     * @return
     */
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<ProductVO> showOneProductByIDAdmin(@PathVariable Long id) {
        ProductVO productVO = productService.showOneByIDAdmin(id);
        return Result.success(productVO);
    }

    /**
     * 添加新产品
     * @param productDTO
     * @param request
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<Integer> addProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request) {
        String token = request.getHeader("token");
        return Result.success(productService.addProduct(productDTO, token));
    }

    /**
     * 修改产品信息
     * @param product
     * @param request
     * @return
     */
    @PutMapping("/change")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<Integer> changeProduct(@RequestBody Product product, HttpServletRequest request) {
        String token = request.getHeader("token");
        return Result.success(productService.changeProduct(product, token));
    }

    /**
     * 逻辑删除
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<Integer> deleteProductByIDs(@PathVariable Long[] ids , HttpServletRequest request){
        String token = request.getHeader("token");
        return Result.success(productService.deleteByIDs(ids, token));
    }

    /**
     * 恢复逻辑删除
     * @param ids
     * @param request
     * @return
     */
    @PutMapping("/recover/{ids}")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<Integer> recoverProductByIDs(@PathVariable Long[] ids , HttpServletRequest request){
        String token = request.getHeader("token");
        return Result.success(productService.recoverByIDS(ids, token));
    }
}
