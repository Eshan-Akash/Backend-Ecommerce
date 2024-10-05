package dev.eshan.productservice.events;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEvent extends BaseEvent {
    GenericCategoryDto category;
}
