package com.project.shopapp.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "djt6sskch",
                "api_key", "543336241935677",
                "api_secret", "bdzqTRG-csWemJfnuI6wYs_n8vI",
                "secure", true));
        return cloudinary;
    }

}
