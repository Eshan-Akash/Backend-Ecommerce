package dev.eshan.orderservice.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart extends BaseModel {

    @Column(nullable = false)
    String userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    List<CartItem> cartItems = new ArrayList<>();

    @Column(nullable = false)
    Double totalPrice = 0.0;

    String discountCode;
    Double appliedDiscount;

    public void addCartItem(CartItem cartItem) {
        if (this.cartItems == null) {
            this.cartItems = new ArrayList<>();
        }
        cartItems.add(cartItem);
    }
}