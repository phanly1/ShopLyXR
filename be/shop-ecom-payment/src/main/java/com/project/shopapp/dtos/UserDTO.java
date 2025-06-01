package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
public class UserDTO {


    private String id;

    private String fullName;

    private String email;


    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private  String address;

    @NotBlank(message = "Password is required")
    private String password;

    private String avt;

    private String sex;

    private boolean active = true;

    private String retypePassword;

    private Date dateOfBirth;

    private int facebookAccountId;

    private int googleAccountId;

    @NotNull
    private String roleName;

}
