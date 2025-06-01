package com.project.shopapp.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "token")
public class Token {
    @Id
    private String id;

    private String token;

    @Field("token_type")
    private String tokenType;

    @Field("expiration_date")
    private LocalDateTime expirationDate;
    private String revoked;
    private String expired;

    @Field("user_id")
    private String userId;
}
