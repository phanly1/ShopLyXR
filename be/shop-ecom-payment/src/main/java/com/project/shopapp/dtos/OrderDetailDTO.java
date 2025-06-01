package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private String id;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("product_id")
    private String productId;

    @Min(value = 0, message = "Price must be greater than >= 0")
    private Float price;

    @JsonProperty("number_of_product")
    @Min(value = 1, message = "Number of product must be greater than or equal 1")
    private int numberOfProduct;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Number of product must be greater than or equal 1")
    private Float totalMoney;

    private String color;
}
