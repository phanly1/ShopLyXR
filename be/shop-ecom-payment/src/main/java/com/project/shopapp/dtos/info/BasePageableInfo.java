package com.project.shopapp.dtos.info;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class    BasePageableInfo implements Serializable {

    // Tìm trong tất cả hoặc mã đơn

    // Từ khóa tìm chung chung
    @ApiModelProperty(value = "Từ khoá tìm kiếm", allowEmptyValue = true, example = "")
    private String keyword;

    // Trang hiện tại.
    @ApiModelProperty(value = "Tra hiện tại. Mặc định là 1", allowEmptyValue = true, example = "2")
    @Min(0)
    private int currentPage = 1;

    // Số bản ghi trên một trang.
    @ApiModelProperty(value = "Số bản ghi phân trang. Mặc định là 20", allowEmptyValue = true, example = "20")
    @Min(1)
    @Max(500)
    private int recordPerPage = 20;

    // Tên trường sắp xếp
    @ApiModelProperty(value = "Trường sắp xếp mặc định createdDate", allowEmptyValue = true, example = "createdDate")
    private String fieldSorted = "createdDate";

    // hướng sưaps xếp asc | desc
    @ApiModelProperty(value = "Hướng sắp xếp. mặc định asc", allowEmptyValue = true, example = "asc")
    private String typeSorted = "asc";
}
