package com.project.lms.service;

import com.project.lms.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);

    // Order -> OrderDTO 변환
    List<OrderDTO> getOrdersByStudentId(String sId);

    void cancelOrder(Long oId);
}
