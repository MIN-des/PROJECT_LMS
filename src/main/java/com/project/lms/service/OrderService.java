package com.project.lms.service;

import com.project.lms.dto.OrderDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    // 학점 부여
    void updateScore(Long oId, Integer score);

    List<Order> getOrdersByCourse_cId(Long cId);

    OrderDTO createOrder(OrderDTO orderDTO);

    // Order -> OrderDTO 변환
    List<OrderDTO> getOrdersByStudent_sId(String sId);

    void cancelOrder(Long oId);
}
