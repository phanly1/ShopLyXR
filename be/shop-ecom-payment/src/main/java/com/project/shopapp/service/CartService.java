package com.project.shopapp.service;

import com.project.shopapp.dtos.info.CartAddInfo;
import com.project.shopapp.dtos.info.CartUpdateInfo;
import com.project.shopapp.models.Cart;

public interface CartService {
    Cart getCart(String userId);

    Cart addProductToCart(String userId, CartAddInfo model);

    Cart updateCart(String userId, CartUpdateInfo model);


    void removeProductFromCart(String userId, CartAddInfo model);

    Float getTotal(String userId);

    int getProductCount(String userId);

    void unSelectProduc(String userId);
}
