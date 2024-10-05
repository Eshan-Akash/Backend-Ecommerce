package dev.eshan.productservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @ManyToOne(cascade = {CascadeType.PERSIST})
    Supplier supplier;
}
