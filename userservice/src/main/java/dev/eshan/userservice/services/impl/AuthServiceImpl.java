package dev.eshan.userservice.services.impl;

import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.models.SessionStatus;
import dev.eshan.userservice.services.interfaces.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Override
    public UserDto login(String email, String password) throws Exception {
        return null;
    }

    @Override
    public void logout(String token, String userId) {

    }

    @Override
    public UserDto signUp(String email, String password) {
        return null;
    }

    @Override
    public SessionStatus validateToken(String token, String userId) {
        return null;
    }
}
