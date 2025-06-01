package com.project.shopapp.service;

import com.project.shopapp.models.Coupon;

import java.util.List;

public interface CouponService {
    List<Coupon> findAllCoupons();

    List<Coupon> findAllActiveCoupons();

    List<Coupon> findAllCouponByStatus(String status);

    Coupon createCoupon(Coupon coupon);

    Coupon updateCoupon(Coupon coupon);

    Coupon findCouponByCode(String code);

    Coupon applyCoupon(String code);

    void deleteCoupon(String code);
}
