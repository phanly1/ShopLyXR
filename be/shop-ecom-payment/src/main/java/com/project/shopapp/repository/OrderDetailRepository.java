package com.project.shopapp.repository;

import com.project.shopapp.models.OrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends MongoRepository<OrderDetail, String> {
//    List<OrderDetail> findAllByOrderId(String id);
}
