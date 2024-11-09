package dev.eshan.orderservice.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private String name;
    private String email;
    private String address;
}
