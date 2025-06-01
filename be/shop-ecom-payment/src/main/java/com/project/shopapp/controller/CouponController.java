package com.project.shopapp.controller;


import com.project.shopapp.models.Coupon;
import com.project.shopapp.models.CouponStatusRequest;
import com.project.shopapp.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/coupons")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/getByActive")
    public ResponseEntity<List<Coupon>> getCouponsByActive(){
        List<Coupon> coupons = couponService.findAllActiveCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable("code") String code){
        Coupon coupon = couponService.findCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Coupon> applyCoupon(@PathVariable("code") String code){
        Coupon coupon = couponService.applyCoupon(code);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping("/add")
    public ResponseEntity<Coupon> addCoupon(@RequestBody Coupon coupon){
        Coupon model = couponService.createCoupon(coupon);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{code}")
    public void deleteCoupon(@PathVariable("code") String code){
        couponService.deleteCoupon(code);
    }

    @PutMapping("/update")
    public ResponseEntity<Coupon> updateCoupon(@RequestBody Coupon model){
       Coupon coupon = couponService.updateCoupon(model);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping("/status")
    public ResponseEntity<List<Coupon>> getCouponsByStatus(@RequestBody Coupon request) {
        List<Coupon> coupons = couponService.findAllCouponByStatus(request.getStatus());
        return ResponseEntity.ok(coupons);
    }
}
