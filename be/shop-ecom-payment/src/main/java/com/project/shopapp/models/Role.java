package com.project.shopapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private String name;

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
}
