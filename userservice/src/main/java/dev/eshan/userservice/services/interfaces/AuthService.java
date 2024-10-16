package dev.eshan.userservice.services.interfaces;

import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.models.SessionStatus;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    UserDto login(String email, String password) throws Exception;
    void logout(String token, String userId);
    UserDto signUp(String email, String password);
    SessionStatus validateToken(String token, String userId);
}
