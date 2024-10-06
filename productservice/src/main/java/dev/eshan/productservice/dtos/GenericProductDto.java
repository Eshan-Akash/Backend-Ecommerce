package dev.eshan.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.eshan.productservice.models.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericProductDto implements Serializable {
    String id;
    String title;
    String description;
    String specifications;
    GenericCategoryDto category;
    String imageUrl;
    Price price;
    Integer stockLevel;
    Integer lowStockThreshold;
    GenericSupplierDto supplier;
}
