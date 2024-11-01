package dev.eshan.orderservice.models;

import dev.eshan.orderservice.dtos.CartItemDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_items")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderItem extends BaseModel {

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public static OrderItem of(CartItemDto cartItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(cartItemDto.getProductId());
        orderItem.setProductName(cartItemDto.getProductName());
        orderItem.setQuantity(cartItemDto.getQuantity());
        orderItem.setPrice(cartItemDto.getPricePerUnit());
        return orderItem;
    }
}