package com.project.shopapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "product_images")
public class ProductImage {
    public static final int MAX_IMAGES = 5;
    @Id
    private String id;

    private String productId;

    private String imageUrl;

}
