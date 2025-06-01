package com.project.shopapp.controller;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.info.OrderPageInfo;
import com.project.shopapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    //Create new order
    @PostMapping()
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
            if(result.hasErrors()) {
                List<String> errMsg = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMsg);
            }
            return  ResponseEntity.ok(orderService.createOrder(orderDTO, token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Get all order
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    //Get list order of user
    @GetMapping("user/{user_id}")
    public  ResponseEntity<?> getListOrderByUserId(
            @PathVariable("user_id") String userId
    ) {
        try {
            return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    //Update order
    @PutMapping()
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO model) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(model));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(orderService.deleteOrderById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("search")
    public ResponseEntity<List<OrderDTO>> searchOrder(@RequestBody OrderPageInfo model) {
        List<OrderDTO> orderDTOS = orderService.search(model);
        return ResponseEntity.ok(orderDTOS);
    }

    @PostMapping("count")
    public ResponseEntity<?> countOrder(@RequestBody OrderPageInfo model) {
        return ResponseEntity.ok(orderService.count(model));
    }
}
