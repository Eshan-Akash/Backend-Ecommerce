package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.proxies.CartDto;
import dev.eshan.productservice.dtos.proxies.CartItemDto;
import dev.eshan.productservice.dtos.proxies.DiscountCodeDto;
import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.repositories.ProductRepository;
import dev.eshan.productservice.services.commons.OkHttpClientService;
import dev.eshan.productservice.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static dev.eshan.productservice.utils.Utils.APPLICATION_JSON;

@Service
public class CartServiceProxyImpl {
    private final OkHttpClientService okHttpClientService;
    private final ProductRepository productRepository;
    private final String orderServiceBaseUrl = "http://localhost:9000";

    public CartServiceProxyImpl(OkHttpClientService okHttpClientService, ProductRepository productRepository) {
        this.okHttpClientService = okHttpClientService;
        this.productRepository = productRepository;
    }

    public CartDto addToCart(String userId, CartItemDto cartItem) throws IOException {
        String productId = cartItem.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        cartItem.setProductName(product.getTitle());
        cartItem.setPricePerUnit(product.getPrice().getPrice());

        String url = orderServiceBaseUrl + "/api/v1/cart/add?userId=" + userId;
        RequestBody requestBody = RequestBody.create(MediaType.parse(APPLICATION_JSON), Utils.gson.toJson(cartItem));
        String response = okHttpClientService.postCall(url, requestBody, new HashMap<>());
        return Utils.gson.fromJson(response, CartDto.class);
    }

    public CartDto updateCartItem(String userId, CartItemDto cartItem) throws IOException {
        String productId = cartItem.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        cartItem.setProductName(product.getTitle());
        cartItem.setPricePerUnit(product.getPrice().getPrice());

        String url = orderServiceBaseUrl + "/api/v1/cart/update?userId=" + userId;
        RequestBody requestBody = RequestBody.create(MediaType.parse(APPLICATION_JSON), Utils.gson.toJson(cartItem));
        String response = okHttpClientService.putCall(url, requestBody, new HashMap<>());
        return Utils.gson.fromJson(response, CartDto.class);
    }

    public void removeCartItem(String userId, String itemId) throws IOException {
        String url = orderServiceBaseUrl + "/api/v1/cart/remove/" + itemId + "?userId=" + userId;
        okHttpClientService.deleteCall(url, "",new HashMap<>());
    }

    public CartDto applyDiscount(String userId, DiscountCodeDto discountCode) throws IOException {
        String url =  orderServiceBaseUrl + "/api/v1/cart/apply-discount?userId=" + userId;
        String response = okHttpClientService.postCall(url, RequestBody.create(MediaType.parse(APPLICATION_JSON),
                Utils.gson.toJson(discountCode)), new HashMap<>());
        return Utils.gson.fromJson(response, CartDto.class);
    }

    public CartDto viewCart(String userId) throws IOException {
        String url = orderServiceBaseUrl + "/api/v1/cart/view?userId=" + userId;
        String response = okHttpClientService.getCall(url, "", new HashMap<>());
        return Utils.gson.fromJson(response, CartDto.class);
    }
}