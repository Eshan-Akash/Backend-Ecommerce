package dev.eshan.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.eshan.productservice.models.Category;
import dev.eshan.productservice.models.Price;
import dev.eshan.productservice.models.Supplier;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
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
    GenericSupplierDto supplier;
}
