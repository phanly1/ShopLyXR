package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements UserDetails {

    @Id
    private String id;

    private String fullName;

    @Indexed(unique = true)
    private String phoneNumber;

    private String address;

    private String password;

//    private String retypePassword;

    private String email;

    private boolean active = true;

    private Date dateOfBirth;

    private int facebookAccountId;

    private int googleAccountId;

    private String avt;

    private String sex;

    @NotNull
    private String roleName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRoleName().toUpperCase()));
//        System.out.println(authorityList);
//        authorityList.add(new SimpleGrantedAuthority("ADMIN"));

        return authorityList;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
