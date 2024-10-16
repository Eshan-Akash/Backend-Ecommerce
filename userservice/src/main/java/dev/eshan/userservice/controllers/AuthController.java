package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.*;
import dev.eshan.userservice.models.SessionStatus;
import dev.eshan.userservice.services.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@Valid @RequestBody SignUpRequestDto request) {
        return authService.signUp(request.getEmail(), request.getPassword());
    }

    @PostMapping("/login")
    public UserDto login(@Valid @RequestBody LoginRequestDto request) throws Exception {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutRequestDto request) {
        authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/validate")
    public SessionStatus validateToken(@Valid @RequestBody ValidateTokenRequestDto request) {
        return authService.validateToken(request.getToken(), request.getUserId());
    }
}