package dev.eshan.productservice.dtos;

import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.models.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockRequestDto {
    String supplierId;
    String productId;
    int restockAmount;
}
