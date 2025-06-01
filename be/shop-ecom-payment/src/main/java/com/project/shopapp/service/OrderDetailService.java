package com.project.shopapp.service;

import com.project.shopapp.dtos.OrderDetailDTO;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailDTO> getOrderDetailsByOrderId(String orderId) ;

    OrderDetailDTO getOrderDetailById(String id);

    OrderDetailDTO createOrderDetail(OrderDetailDTO model);

    OrderDetailDTO updateOrderDetail(OrderDetailDTO model);

    OrderDetailDTO deleteOrderDetailById(String id);
}
