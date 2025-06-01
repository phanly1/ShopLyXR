package com.project.shopapp.models;

import lombok.Data;

@Data
public class PaymentRequest {
    private long amount;
    private String orderInfo;
    private String returnUrl;
}
