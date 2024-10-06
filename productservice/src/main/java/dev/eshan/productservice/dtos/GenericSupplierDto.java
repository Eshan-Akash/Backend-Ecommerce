package dev.eshan.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericSupplierDto {
    String id;
    String name;
    String contactPersonName;
    String contactEmail;
    String contactPhone;
    String address;
}
