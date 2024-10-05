package dev.eshan.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.eshan.productservice.models.Price;
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
    GenericCategoryDto category;
    String image;
    Price price;
}
