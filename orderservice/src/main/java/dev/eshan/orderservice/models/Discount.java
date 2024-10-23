package dev.eshan.orderservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "discounts")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Discount extends BaseModel {

    @Column(nullable = false, unique = true)
    String code;

    @Column(nullable = false)
    Double discountPercentage;

    @Column(nullable = false)
    Boolean isActive;
}
