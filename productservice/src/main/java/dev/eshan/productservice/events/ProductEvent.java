package dev.eshan.productservice.events;

import dev.eshan.productservice.dtos.GenericProductDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent extends BaseEvent{
    GenericProductDto product;
}
