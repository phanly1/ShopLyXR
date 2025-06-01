package com.project.shopapp.repository;

import com.project.shopapp.models.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CouponRepository extends MongoRepository<Coupon, String> {
    List<Coupon> findByActiveTrue();
    List<Coupon> findAllByStatus(String status);
    boolean existsByCode(String code);
    Coupon findByCode(String code);
}
