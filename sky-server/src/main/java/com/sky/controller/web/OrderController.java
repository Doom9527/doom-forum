package com.sky.controller.web;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
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
    @GetMapping("/showAll")
    @PreAuthorize("hasAuthority('system:operation:user')")
    public Result<List<OrdersVO>> showAllOrders() {
        List<OrdersVO> ordersVOS = ordersService.selectAll();
        return Result.success(ordersVOS);
    }

    /**
     * 按id查询订单
     * @param id
     * @return
     */
    @GetMapping("/showOne/{id}")
    @PreAuthorize("hasAuthority('system:operation:user')")
    public Result<OrdersVO> showOne(@PathVariable Long id) {
        return Result.success(ordersService.selectByID(id));
    }

    /**
     * 下单
     * @param ordersDTO
     * @param request
     * @return
     */
    @PostMapping("/place")
    @PreAuthorize("hasAuthority('system:operation:user')")
    public Result<OrdersVO> placeNewOrder(@RequestBody @Valid OrdersDTO ordersDTO, HttpServletRequest request){
        String token = request.getHeader("token");
        return Result.success(ordersService.placeNewOrder(ordersDTO, token));
    }

    /**
     * 支付
     * @param number
     * @param request
     * @return
     */
    @PutMapping("/{number}")
    @PreAuthorize("hasAuthority('system:operation:user')")
    public Result<Integer> payOrder(@PathVariable String number, HttpServletRequest request) {
        return Result.success(ordersService.payOrder(number));
    }

    /**
     * 手动取消订单
     * @param ordersCancelDTO
     * @return
     */
    @DeleteMapping("/cancel")
    @PreAuthorize("hasAuthority('system:operation:user')")
    public Result<Integer> cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        return Result.success(ordersService.cancelOrder(ordersCancelDTO));
    }

}
