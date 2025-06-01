//package com.project.shopapp.controller;
//
//import com.project.shopapp.dtos.OrderDetailDTO;
//import com.project.shopapp.service.OrderDetailService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/order_detail")
//public class OrderDetailController {
//    private final OrderDetailService orderDetailService;
//
//    // Get order with id
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("id") String id) {
//        try {
//            return ResponseEntity.ok(orderDetailService.getOrderDetailById(id));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    //Get list order details with orderID
//    @GetMapping("/order/{order_id}")
//    public ResponseEntity<?> getListOrderDetail(@Valid @PathVariable("order_id") String orderId) {
//        return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
//    }
//
//    //Add oderDetail
//    @PostMapping()
//    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
//        try {
//            return ResponseEntity.ok(orderDetailService.createOrderDetail(orderDetailDTO));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
//
//    //Update orderDetail
//    @PutMapping()
//    public ResponseEntity<?> updateOrderDetail (
//            @RequestBody OrderDetailDTO orderDetailDTO
//    ) {
//        return ResponseEntity.ok(orderDetailService.updateOrderDetail(orderDetailDTO));
//    }
//
//    //Delete orderDetail
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteOrderDetailById (@Valid @PathVariable("id") String id) {
//        return ResponseEntity.ok(orderDetailService.deleteOrderDetailById(id));
//    }
//
//}
