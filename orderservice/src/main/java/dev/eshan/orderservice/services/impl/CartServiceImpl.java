package dev.eshan.orderservice.services.impl;

import dev.eshan.orderservice.dtos.CartDto;
import dev.eshan.orderservice.dtos.CartItemDto;
import dev.eshan.orderservice.dtos.DiscountCodeDto;
import dev.eshan.orderservice.models.Cart;
import dev.eshan.orderservice.models.CartItem;
import dev.eshan.orderservice.models.Discount;
import dev.eshan.orderservice.repositories.CartItemRepository;
import dev.eshan.orderservice.repositories.CartRepository;
import dev.eshan.orderservice.repositories.DiscountRepository;
import dev.eshan.orderservice.services.interfaces.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DiscountRepository discountRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, DiscountRepository discountRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public CartDto addToCart(String userId, CartItemDto cartItemDto) {
        // Find or create the cart for the given user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        // Check if the item already exists in the cart
        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartAndProductId(cart, cartItemDto.getProductId());

        if (existingCartItemOpt.isPresent()) {
            log.info("Item already exists in cart");
            // Update quantity if item exists
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(cartItemDto.getQuantity());
        } else {
            log.info("Item does not exist in cart");
            // Create new cart item if it doesn't exist
            CartItem cartItem = new CartItem();
            cartItem.setProductId(cartItemDto.getProductId());
            cartItem.setProductName(cartItemDto.getProductName());
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setPricePerUnit(cartItemDto.getPricePerUnit());
            cartItem.setCart(cart);

            cartItemRepository.save(cartItem);
            cart.addCartItem(cartItem);
        }

        // reset the applied discount and discount code
        cart.setAppliedDiscount(0.0);
        cart.setDiscountCode(null);

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
        cart.setFinalPrice(totalPrice);
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
    }

    private CartDto createCartDto(Cart cart) {
        double totalOriginalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPricePerUnit() * item.getQuantity())
                .sum();
        List<CartItemDto> cartItemDtoList = cart.getCartItems().stream()
                .map(CartItemDto::of)
                .collect(Collectors.toList());
        return new CartDto(cart.getUserId(),
                cartItemDtoList,
                totalOriginalPrice,
                cart.getDiscountCode(),
                cart.getAppliedDiscount());
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
        cart.setFinalPrice(updatedTotalPrice);
        cart.setTotalPrice(updatedTotalPrice);

        // reset the applied discount and discount code
        cart.setAppliedDiscount(0.0);
        cart.setDiscountCode(null);

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
        cart.setFinalPrice(updatedTotalPrice);
        cart.setTotalPrice(updatedTotalPrice);

        // Save the updated cart in the repository
        cartRepository.save(cart);
    }

    @Override
    public CartDto applyDiscount(String userId, DiscountCodeDto discountCodeDto) {
        // Step 1: Retrieve the cart associated with the user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Step 2: Validate the discount code
        Discount discount = discountRepository.findByCode(discountCodeDto.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or non-existent discount code"));

        if (!Boolean.TRUE.equals(discount.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Discount code is inactive");
        }

        // Step 3: Calculate the discount and apply it to the cart
        double discountAmount = cart.getTotalPrice() * (discount.getDiscountPercentage() / 100.0);
        double finalPrice = cart.getTotalPrice() - discountAmount;

        // Set the applied discount details in the cart
        cart.setFinalPrice(finalPrice);
        cart.setAppliedDiscount(discountAmount);
        cart.setDiscountCode(discountCodeDto.getCode());

        // Save the cart with the applied discount
        cartRepository.save(cart);

        // Step 4: Convert to DTO and return the updated cart
        return createCartDto(cart);
    }

    @Override
    public CartDto viewCart(String userId) {
        // Step 1: Retrieve the cart associated with the user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Step 2: Convert the cart entity to a CartDto
        return createCartDto(cart);
    }

    @Override
    public void clearCart(String userId) {
        // Find the cart by user ID
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            // Remove all items in the cart
            cartItemRepository.deleteAll(cart.getCartItems());
            // Optionally, clear the cart items list and reset totals
            cart.getCartItems().clear();
            cart.setFinalPrice(0.0);
            cart.setDiscountCode(null);
            cart.setAppliedDiscount(0.0);
            cart.setFinalPrice(0.0);
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);
        }
    }
}
