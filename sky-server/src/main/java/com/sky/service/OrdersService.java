package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrdersVO;

import java.util.List;

public interface OrdersService extends IService<Orders> {
    List<OrdersVO> selectAll();

    OrdersVO selectByID(Long id);

    OrdersVO placeNewOrder(OrdersDTO ordersDTO, String token);

    int payOrder(String number);

    int cancelOrder(OrdersCancelDTO ordersCancelDTO);

    void timeOutCancelOrder(String number);
}
