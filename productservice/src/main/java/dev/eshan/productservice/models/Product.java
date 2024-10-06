package dev.eshan.productservice.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class Product extends BaseModel {
    String title;
    String description;
    String specifications;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    Category category;
    String imageUrl;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    Price price;

    Integer stockLevel;
    Integer lowStockThreshold;

    @ManyToOne
    @JoinColumn(name = "supplier_id")  // Foreign key to Supplier
    Supplier supplier;  // Each product has one supplier
}
