package dev.eshan.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogoutRequestDto {
    @NotBlank(message = "Token is required")
    private String token;
    @NotNull(message = "User ID is required")
    private String userId;
}
