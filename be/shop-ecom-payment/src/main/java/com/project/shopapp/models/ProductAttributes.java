package com.project.shopapp.models;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "productAttributes")
public class ProductAttributes {
    @Id
    private String id;

    private String code;

    private String key;
    private List<String> value;

    public ProductAttributes(String key, List<String> value) {
        this.key = key;
        this.value = value;
    }
    public ProductAttributes() {}
}
