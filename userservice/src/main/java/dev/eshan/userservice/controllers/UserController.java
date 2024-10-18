package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.ChangePasswordRequestDto;
import dev.eshan.userservice.dtos.UpdateUserProfileRequestDto;
import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.services.impl.UserServiceImpl;
import dev.eshan.userservice.services.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUserDetails(@PathVariable("id") String userId) {
        return userService.getUserDetails(userId);
    }

    @PutMapping("/{id}/profile")
    public UserDto updateUserProfile(@PathVariable("id") String userId, @Valid @RequestBody UpdateUserProfileRequestDto request) {
        return userService.updateUserProfile(userId, request);
    }

    @PutMapping("/{id}/change-password")
    public void changePassword(@PathVariable("id") String userId, @Valid @RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(userId, request);
    }
}
