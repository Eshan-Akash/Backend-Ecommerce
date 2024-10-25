package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CartItemDto;
import dev.eshan.orderservice.dtos.DiscountCodeDto;
import dev.eshan.orderservice.models.Cart;
import dev.eshan.orderservice.models.CartItem;
import dev.eshan.orderservice.repositories.CartItemRepository;
import dev.eshan.orderservice.repositories.CartRepository;
import dev.eshan.orderservice.services.interfaces.CartService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartDto addToCart(String userId, CartItemDto cartItemDto) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemDto.getProductId());
        cartItem.setProductName(cartItemDto.getProductName());
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setPrice(cartItemDto.getPricePerUnit());

        cartItem.setCart(cart);

        cartItemRepository.save(cartItem);

        updateCartTotalPrice(cart);

        return createCartDto(cart);
    }

    private Cart createNewCart(String userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        return cartRepository.save(newCart);
    }

    private void updateCartTotalPrice(Cart cart) {
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
    }

    private CartDto createCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setUserId(cart.getUserId());
        cartDto.setCartItems(cart.getCartItems().stream()
                .map(item -> CartItemDto.of(item))
                .collect(Collectors.toList()));
        cartDto.setTotalOriginalPrice(cart.getTotalPrice());
        cartDto.setFinalPrice(cart.getTotalPrice());
        return cartDto;
    }

    @Override
    public CartDto updateCartItem(String userId, CartItemDto cartItem) {
        return null;
    }

    @Override
    public void removeCartItem(String userId, String itemId) {

    }

    @Override
    public CartDto applyDiscount(String userId, DiscountCodeDto discountCode) {
        return null;
    }

    @Override
    public CartDto viewCart(String userId) {
        return null;
    }
}
