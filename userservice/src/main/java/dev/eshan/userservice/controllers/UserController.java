package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.ChangePasswordRequestDto;
import dev.eshan.userservice.dtos.UpdateUserProfileRequestDto;
import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.services.impl.UserServiceImpl;
import dev.eshan.userservice.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    private UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUserDetails(@PathVariable("id") String userId) {
        try {
            return userService.getUserDetails(userId);
        } catch (Exception e) {
            log.error("Error occurred while fetching user details of id: {}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching user details");
        }
    }

    @PutMapping("/{id}/profile")
    public UserDto updateUserProfile(@PathVariable("id") String userId, @Valid @RequestBody UpdateUserProfileRequestDto request) {
        try {
            return userService.updateUserProfile(userId, request);
        } catch (Exception e) {
            log.error("Error occurred while updating user profile of id: {}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while updating user profile");
        }
    }

    @PutMapping("/{id}/change-password")
    public void changePassword(@PathVariable("id") String userId, @Valid @RequestBody ChangePasswordRequestDto request) {
        try {
            userService.changePassword(userId, request);
        } catch (Exception e) {
            log.error("Error occurred while changing password of id: {}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while changing password");
        }
    }
}
