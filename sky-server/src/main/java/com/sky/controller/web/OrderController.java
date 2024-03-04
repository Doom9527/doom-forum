package com.sky.controller.web;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.PageDTO;
import com.sky.result.PageQuery;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrdersVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 查看所有订单
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<PageDTO<OrdersVO>> showAllOrders(@Valid PageQuery query) {
        PageDTO<OrdersVO> ordersVOS = ordersService.selectAll(query);
        return Result.success(ordersVOS);
    }

    /**
     * 按id查询订单
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<OrdersVO> showOne(@PathVariable Long id) {
        return Result.success(ordersService.selectByID(id));
    }

    /**
     * 下单
     * @param ordersDTO
     * @param request
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<OrdersVO> placeNewOrder(@RequestBody @Valid OrdersDTO ordersDTO, HttpServletRequest request){
        String token = request.getHeader("token");
        return Result.success(ordersService.placeNewOrder(ordersDTO, token));
    }

    /**
     * 支付
     * @param number
     * @return
     */
    @PutMapping("/pay/{number}")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<Integer> payOrder(@PathVariable String number) {
        return Result.success(ordersService.payOrder(number));
    }

    /**
     * 手动取消订单
     * @param ordersCancelDTO
     * @return
     */
    @DeleteMapping("/cancel")
    @PreAuthorize("hasAuthority('system:operation:dept')")
    public Result<Integer> cancelOrder(@RequestBody @Valid OrdersCancelDTO ordersCancelDTO) {
        return Result.success(ordersService.cancelOrder(ordersCancelDTO));
    }

}
