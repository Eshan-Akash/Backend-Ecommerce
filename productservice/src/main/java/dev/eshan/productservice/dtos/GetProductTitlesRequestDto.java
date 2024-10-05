package dev.eshan.productservice.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GetProductTitlesRequestDto {
    private List<String> ids;
}
