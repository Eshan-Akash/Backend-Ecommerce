package dev.eshan.orderservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Order extends BaseModel {

    @Column(nullable = false)
    String customerId;

    @Column(nullable = false)
    Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<CartItem> cartItems;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.ALL)
    Payment payment;
}
