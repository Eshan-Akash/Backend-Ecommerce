package dev.eshan.userservice.services.interfaces;

import dev.eshan.userservice.dtos.ChangePasswordRequestDto;
import dev.eshan.userservice.dtos.SignUpRequestDto;
import dev.eshan.userservice.dtos.UpdateUserProfileRequestDto;
import dev.eshan.userservice.dtos.UserDto;

public interface UserService {
    UserDto getUserDetails(String userId);
    UserDto updateUserProfile(String userId, UpdateUserProfileRequestDto request);
    void changePassword(String userId, ChangePasswordRequestDto request);
}
