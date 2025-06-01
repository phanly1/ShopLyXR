package com.project.shopapp.repository;

import com.project.shopapp.dtos.info.OrderPageInfo;
import com.project.shopapp.models.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> search(OrderPageInfo model);

    Long count(OrderPageInfo model);
}
