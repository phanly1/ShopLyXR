package com.project.shopapp.controller;

import com.project.shopapp.dtos.info.CartAddInfo;
import com.project.shopapp.dtos.info.CartUpdateInfo;
import com.project.shopapp.models.Cart;
import com.project.shopapp.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Cart cart =  cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable String userId, @RequestBody @Valid CartAddInfo model) {
        Cart cart = cartService.addProductToCart(userId, model);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/remove/{userId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable String userId, @RequestBody @Valid CartAddInfo model) {
        cartService.removeProductFromCart(userId, model);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @GetMapping("/total-price/{userId}")
    public float getTotalPrice(@PathVariable String userId) {
        return cartService.getTotal(userId);
    }

    @PostMapping("/count/{userId}")
    public int countCart(@PathVariable String userId) {
        return  cartService.getProductCount(userId);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<Cart> updateCart(@PathVariable("userId") String userId ,@RequestBody @Valid CartUpdateInfo cart) {
        cartService.updateCart(userId, cart);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/unSelected/{userId}")
    public ResponseEntity<Cart> unSelectedCart(@PathVariable String userId) {
        cartService.unSelectProduc(userId);
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
