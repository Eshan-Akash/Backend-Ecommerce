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
@Table(name = "payments")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Payment extends BaseModel {

    @Column(nullable = false)
    String paymentGateway;

    @Column(nullable = false)
    Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PaymentStatus paymentStatus;

    @OneToOne(mappedBy = "payment")
    Order order;
}
