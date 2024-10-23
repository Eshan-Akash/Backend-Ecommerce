package dev.eshan.orderservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cart_items")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CartItem extends BaseModel {

    @Column(nullable = false)
    String productId;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false)
    Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;
}
