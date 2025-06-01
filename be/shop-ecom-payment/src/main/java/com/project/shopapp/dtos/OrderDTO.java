package com.project.shopapp.dtos;

import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.OrderStatusHistory;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private String id;

    private String userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private String note;

    private String status;

    private Date orderDate;

    private  Float totalMoney;

    private String feeShip;

    private String paymentMethod;

    private List<OrderDetail> orderDetails;

    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    private boolean active;



//    private String shippingMethod;

//    private Date shippingDate;

//    private String shippingAddress;

//    private String paymentMethod;


}
