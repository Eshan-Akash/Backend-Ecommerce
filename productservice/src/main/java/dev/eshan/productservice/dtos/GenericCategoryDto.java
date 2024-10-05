package dev.eshan.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericCategoryDto {
    private String id;
    private String name;
    private List<GenericProductDto> products;
}
