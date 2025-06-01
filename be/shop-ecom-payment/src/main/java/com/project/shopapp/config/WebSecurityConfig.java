package com.project.shopapp.config;

import com.project.shopapp.helper.JwtTokenFilter;
import com.project.shopapp.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/users/current", apiPrefix),
                                    String.format("%s/users/upload/**", apiPrefix),
                                    String.format("%s/users/change-password/**", apiPrefix),
                                    String.format("%s/auth/login", apiPrefix)
                                    ).permitAll()
                            .requestMatchers(
                                    String.format("%s/attributes/**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/coupons/**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/coupons/**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(DELETE,
                                    String.format("%s/coupons/**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/payment/vn-pay", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/cart/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/cart/**", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(PUT,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/products/upload/{id}", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/products/upload-list/", apiPrefix)).permitAll()
                            .requestMatchers(PUT,
                                    String.format("%s/products", apiPrefix)).permitAll()
                            .requestMatchers(PUT,
                                    String.format("%s/products/update-text-search", apiPrefix)).permitAll()
                            .requestMatchers(DELETE,
                                    String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(PUT,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/comments/**", apiPrefix)).permitAll()
                            .requestMatchers(GET,
                                    String.format("%s/comments/{id}", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Constants.ROLE.USER)
                            .requestMatchers(GET,
                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Constants.ROLE.USER, Constants.ROLE.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Constants.ROLE.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Constants.ROLE.ADMIN)
                            .requestMatchers(GET,
                                    String.format("%s/users", apiPrefix)).hasRole(Constants.ROLE.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/users/update/status", apiPrefix)).hasRole(Constants.ROLE.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/users/reset-password", apiPrefix)).hasRole(Constants.ROLE.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/users/update", apiPrefix)).hasRole(Constants.ROLE.USER)
                            .anyRequest().authenticated();
                }).csrf(AbstractHttpConfigurer::disable);

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("*"));
                corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
                corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", corsConfiguration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
