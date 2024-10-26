package dev.eshan.orderservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
    Boolean paymentSuccess;

    @OneToOne(mappedBy = "payment")
    Order order;
}
