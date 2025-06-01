package com.project.shopapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "order_detail")
public class OrderDetail {
    private String productId;
    private int quantity;
    private float totalPrice;
    private int totalProduct;
}
