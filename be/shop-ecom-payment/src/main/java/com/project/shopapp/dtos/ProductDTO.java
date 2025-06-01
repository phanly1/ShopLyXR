package com.project.shopapp.dtos;

import com.project.shopapp.models.ProductAttributes;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private String id;
    @NotBlank(message = "Name product is not empty")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 character")
    private String name;

    @Min(value = 0, message = "This price must be greater than or equal to 0")
    private Float price;
    private String thumbnail;
    private String description;
    private boolean favourite;
    private String categoryId; //thoitrangphukiennam
    private String subCategoryId;// aopolo
    private int totalProduct;
    private List<String> listThumbnail = new ArrayList<>();
    private int quantity;
    private List<ProductAttributes> attributes = new ArrayList<>();
    private String textSearch;

    public void makeTextSearch() {
        String textSearch = this.name;
        textSearch = textSearch.toLowerCase();
        textSearch = StringUtils.stripAccents(textSearch.toLowerCase());

        this.setTextSearch(textSearch);

    }
}
