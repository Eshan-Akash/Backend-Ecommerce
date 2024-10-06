package dev.eshan.productservice.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class Supplier extends BaseModel{
    String name;
    String contactPersonName;
    String contactEmail;
    String contactPhone;
    String address;
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    List<Product> products;  // One supplier has many products
}

