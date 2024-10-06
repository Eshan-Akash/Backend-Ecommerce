package dev.eshan.productservice.events;

import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.GenericSupplierDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierEvent extends BaseEvent {
    GenericSupplierDto supplier;
    GenericProductDto product;
}
