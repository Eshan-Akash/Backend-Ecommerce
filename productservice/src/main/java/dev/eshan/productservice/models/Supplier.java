package dev.eshan.productservice.models;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
}

