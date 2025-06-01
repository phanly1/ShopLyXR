package com.project.shopapp.models;


import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document(collection = "coupons")
public class Coupon {
    @Id
    private String id;
    private String name;
    //tu dong generate code
    private String code;
    //so luong dung ma
    private int quantity;
    private String discountPercent;
    private String maxDiscountAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active = false;
    private String status = "INACTIVE";
}


