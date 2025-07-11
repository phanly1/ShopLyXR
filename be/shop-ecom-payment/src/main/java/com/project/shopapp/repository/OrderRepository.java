package com.project.shopapp.repository;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>, OrderRepositoryCustom {
    List<OrderDTO> findAllByUserId(String userId);
}
