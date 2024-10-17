package dev.eshan.userservice.services.impl;

import dev.eshan.userservice.dtos.ChangePasswordRequestDto;
import dev.eshan.userservice.dtos.SignUpRequestDto;
import dev.eshan.userservice.dtos.UpdateUserProfileRequestDto;
import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.models.Role;
import dev.eshan.userservice.models.User;
import dev.eshan.userservice.repositories.RoleRepository;
import dev.eshan.userservice.repositories.UserRepository;
import dev.eshan.userservice.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public UserDto getUserDetails(String userId) {
        return null;
    }

    @Override
    public UserDto updateUserProfile(String userId, UpdateUserProfileRequestDto request) {
        return null;
    }

    @Override
    public void changePassword(String userId, ChangePasswordRequestDto request) {

    }
}
