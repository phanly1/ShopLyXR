package com.project.shopapp.dtos.info;

import lombok.Data;

@Data
public class CartUpdateInfo {
    private String productId;
    private int quantity;
    private Float totalPrice;
}
