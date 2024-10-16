package dev.eshan.userservice.dtos;

import dev.eshan.userservice.models.Role;
import dev.eshan.userservice.models.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    String profileImageUrl;
    Set<String> roles = new HashSet<>();
    String token;
    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImageUrl(user.getProfileImageUrl())
                .roles(user.getRoles().stream()
                        .map(Role::getRole) // Assuming the `Role` entity has a `getName()` method
                        .collect(Collectors.toSet()))
                .build();
    }
}
