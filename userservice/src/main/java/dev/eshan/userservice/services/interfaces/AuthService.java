package dev.eshan.userservice.services.interfaces;

import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.models.SessionStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    UserDto signUp(String email, String password);
    UserDto login(String email, String password, HttpServletResponse response) throws Exception;
    void logout(String token, String userId);
    SessionStatus validateToken(String token, String userId);
}
