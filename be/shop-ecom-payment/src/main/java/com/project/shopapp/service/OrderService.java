package com.project.shopapp.service;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.info.OrderPageInfo;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Order;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrder();

    OrderDTO getOrderById(String id);

    List<OrderDTO> getOrdersByUserId(String userId);

    OrderDTO createOrder(OrderDTO model, String token) throws DataNotFoundException;

    OrderDTO updateOrder(OrderDTO model);

    OrderDTO deleteOrderById(String id);

    List<OrderDTO> search(OrderPageInfo model);
    Long count(OrderPageInfo model);
}
