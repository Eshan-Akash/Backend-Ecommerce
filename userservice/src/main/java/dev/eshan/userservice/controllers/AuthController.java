package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.*;
import dev.eshan.userservice.models.SessionStatus;
import dev.eshan.userservice.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@Valid @RequestBody SignUpRequestDto request) {
        try {
            return authService.signUp(request.getEmail(), request.getPassword());
        } catch (Exception e) {
            log.error("Error while signing up", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public UserDto login(@Valid @RequestBody LoginRequestDto request, HttpServletResponse response) throws Exception {
        try {
            return authService.login(request.getEmail(), request.getPassword(), response);
        } catch (Exception e) {
            log.error("Error while logging in", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutRequestDto request) {
        try {
            authService.logout(request.getToken(), request.getUserId());
        } catch (Exception e) {
            log.error("Error while logging out", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/validate")
    public SessionStatus validateToken(@Valid @RequestBody ValidateTokenRequestDto request) {
        try {
            return authService.validateToken(request.getToken(), request.getUserId());
        } catch (Exception e) {
            log.error("Error while validating token", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}