package dev.eshan.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequestDto {
    @NotNull(message = "User ID is required")
    Long userId;
    @NotBlank(message = "Current password is required.")
    String currentPassword;

    @NotBlank(message = "New password is required.")
    @Size(min = 8, message = "New password must be at least 8 characters long.")
    String newPassword;

    @NotBlank(message = "Please confirm your new password.")
    @Size(min = 8, message = "Password confirmation must be at least 8 characters long.")
    String confirmPassword;
}
