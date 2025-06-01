package com.project.shopapp.models;
import org.apache.commons.lang3.StringUtils;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    @Size(min = 3, max = 200)
    @NotBlank
    private String name;

    @Min(0)
    private Float price;

    private String thumbnail;

    private String description;

    private boolean favourite = false;

    //Ma parent
    private String categoryId;
    //Ma child
    private String subCategoryId;

    @Min(0)
    @Max(10000)
    //so luong san pham mua
    private int quantity;

    //Tong so luong san pham do admin nhap
    private int totalProduct;

    // Danh sach thumbnail
    private List<String> listThumbnail = new ArrayList<>();

    private List<ProductAttributes> attributes = new ArrayList<>();

    private String textSearch;

    public void makeTextSearch() {
        String textSearch = this.name;
        textSearch = textSearch.toLowerCase();
        textSearch = StringUtils.stripAccents(textSearch.toLowerCase());

        this.setTextSearch(textSearch);

    }

}
