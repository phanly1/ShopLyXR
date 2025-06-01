package com.project.shopapp.dtos.info;

import lombok.Data;

@Data
public class CartAddInfo {
    private String productId;
    private int quantity;
    private boolean selected;
}
