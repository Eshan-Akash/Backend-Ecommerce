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

import java.util.List;
import java.util.Optional;
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
        // Find or create the cart for the given user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        // Check if the item already exists in the cart
        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartAndProductId(cart, cartItemDto.getProductId());

        if (existingCartItemOpt.isPresent()) {
            // Update quantity if item exists
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(cartItemDto.getQuantity());
        } else {
            // Create new cart item if it doesn't exist
            CartItem cartItem = new CartItem();
            cartItem.setProductId(cartItemDto.getProductId());
            cartItem.setProductName(cartItemDto.getProductName());
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setPricePerUnit(cartItemDto.getPricePerUnit());
            cartItem.setCart(cart);

            cartItemRepository.save(cartItem);
        }

        // Update the cart's total price after modifying items
        updateCartTotalPrice(cart);

        // Return the updated cart details as a DTO
        return createCartDto(cart);
    }

    private Cart createNewCart(String userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        return cartRepository.save(newCart);
    }

    private void updateCartTotalPrice(Cart cart) {
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPricePerUnit())
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
    public CartDto updateCartItem(String userId, CartItemDto cartItemDto) {
        // Retrieve the cart for the specified user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Check if the cart item exists in the user's cart
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(cartItemDto.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found for product ID: " + cartItemDto.getProductId()));

        // Update the cart item details
        existingCartItem.setQuantity(cartItemDto.getQuantity());
        existingCartItem.setPricePerUnit(cartItemDto.getPricePerUnit());

        // Save updated cart item to repository
        cartItemRepository.save(existingCartItem);

        // Update the total price for the cart
        double updatedTotalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPricePerUnit() * item.getQuantity())
                .sum();
        cart.setTotalPrice(updatedTotalPrice);

        // Save the updated cart to repository
        cartRepository.save(cart);

        // Map the updated cart to a CartDto to return as a response
        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(CartItemDto::of)
                .collect(Collectors.toList());

        return new CartDto(userId,
                cartItems,
                updatedTotalPrice,
                null,
                0.0);
    }

    @Override
    public void removeCartItem(String userId, String itemId) {
        // Retrieve the cart for the specified user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Find the cart item to be removed or decreased in quantity
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found with ID: " + itemId));

        // Decrease quantity or remove item if quantity is 1
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        // Update the cart's total price
        double updatedTotalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPricePerUnit() * item.getQuantity())
                .sum();
        cart.setTotalPrice(updatedTotalPrice);

        // Save the updated cart in the repository
        cartRepository.save(cart);
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
