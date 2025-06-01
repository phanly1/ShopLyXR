package com.project.shopapp.models;

import lombok.Data;

import java.util.Date;

@Data
public class OrderStatusHistory {
    private String status;
    private Date time;

    public OrderStatusHistory(String status, Date time) {
        this.status = status;
        this.time = time;
    }

    public OrderStatusHistory() {

    }
}
