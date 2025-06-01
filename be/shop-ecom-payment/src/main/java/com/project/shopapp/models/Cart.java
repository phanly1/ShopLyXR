package com.project.shopapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "cart")
public class Cart {
    @Id
    String id;

    String userId;

    private List<CartItem> items = new ArrayList<>();

    @Data
    public static class CartItem {
        private String productId;
        private Float totalPrice;
        private int quantity;
        private boolean selected = false;
    }
}
