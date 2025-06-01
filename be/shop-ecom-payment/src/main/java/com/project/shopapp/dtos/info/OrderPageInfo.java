package com.project.shopapp.dtos.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPageInfo extends BasePageableInfo{
    private String status;
}
