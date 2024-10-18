package dev.eshan.userservice.dtos;

import lombok.Data;

@Data
public class ValidateTokenRequestDto {
    private String token;
    private String userId;
}
