package com.project.shopapp.service.imp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.dtos.info.CartAddInfo;
import com.project.shopapp.dtos.info.CartUpdateInfo;
import com.project.shopapp.models.Cart;
import com.project.shopapp.models.Product;
import com.project.shopapp.repository.CartRepository;
import com.project.shopapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    public Product getProductById(String productId) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8088/api/v1/products/" + productId ))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
//        JsonNode productNode = root.get("data").get(0); // Lấy node đại diện cho Product

        Product product = mapper.treeToValue(root, Product.class);
        return product;
    }

    @Override
    public Cart getCart(String userId) {

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });
    }
    //todo: lam viec voi service product
    @Override
    public Cart addProductToCart(String userId, CartAddInfo model) {
        Cart cart = getCart(userId);
        Product product = null;
        try {
            product = getProductById(model.getProductId());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Check if the product already exists in the cart
        boolean productExistsInCart = false;
        for (Cart.CartItem item : cart.getItems()) {
            if (item.getProductId().equals(model.getProductId())) {
                // Product exists, update the quantity, total price, and selected status
                int newQuantity = item.getQuantity() + model.getQuantity();
                float newTotalPrice = product.getPrice() * newQuantity;
                item.setQuantity(newQuantity);
                item.setTotalPrice(newTotalPrice);
                item.setSelected(model.isSelected()); // Update the selected status
                productExistsInCart = true;
                break;
            }
        }

        // If the product does not exist in the cart, add it
        if (!productExistsInCart) {
            float totalPrice = product.getPrice() * model.getQuantity();
            Cart.CartItem cartItem = new Cart.CartItem();
            cartItem.setProductId(model.getProductId());
            cartItem.setTotalPrice(totalPrice);
            cartItem.setQuantity(model.getQuantity());
            cartItem.setSelected(model.isSelected()); // Set the selected status
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }



    @Override
    public Cart updateCart(String userId, CartUpdateInfo model) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isEmpty()) {
            throw new RuntimeException("Cart not found for user: " + userId);
        }

        Cart cart = optionalCart.get();

        // Find the cart item that matches the productId
        Optional<Cart.CartItem> cartItemOptional = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(model.getProductId()))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            Cart.CartItem cartItem = cartItemOptional.get();
            // Update the quantity and totalPrice
            cartItem.setQuantity(model.getQuantity());
            cartItem.setTotalPrice(model.getTotalPrice());
        } else {
            throw new RuntimeException("Product not found in cart: " + model.getProductId());
        }

        // Save the updated cart back to the repository
        cartRepository.save(cart);

        return cart;
    }

    @Override
    public void removeProductFromCart(String userId, CartAddInfo model) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(model.getProductId()));
        cartRepository.save(cart);
    }

    @Override
    public Float getTotal(String userId) {
        Cart cart = getCart(userId);
        return cart.getItems().stream()
                .map(Cart.CartItem::getTotalPrice)
                .reduce(0f, Float::sum);
    }
    @Override
    public int getProductCount(String userId) {
        Cart cart = getCart(userId);
        return cart.getItems().size(); // Số lượng sản phẩm khác nhau
    }

    @Override
    public void unSelectProduc(String userId) {
        Cart cart = getCart(userId);

        for (Cart.CartItem item : cart.getItems()) {
            item.setSelected(false);
        }

        cartRepository.save(cart);
    }


}
