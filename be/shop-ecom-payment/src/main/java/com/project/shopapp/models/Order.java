package com.project.shopapp.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "order")
public class Order {
    @Id
    private String id;

    private String userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private String note;

    private String status;

    private Date orderDate;

    private String feeShip;

    private String paymentMethod;

    private  Float totalMoney;

    private List<OrderDetail> orderDetails;

    private boolean active;

    private List<OrderStatusHistory> statusHistory = new ArrayList<>();



//    private String shippingMethod;

//    private String shippingAddress;

//    private Date shippingDate;

//    private String trackingNumber;

//    private String paymentMethod;


}
