package dev.eshan.userservice.services.impl;

import dev.eshan.userservice.dtos.UserDto;
import dev.eshan.userservice.exceptions.UserAlreadyExistsException;
import dev.eshan.userservice.models.Session;
import dev.eshan.userservice.models.SessionStatus;
import dev.eshan.userservice.models.User;
import dev.eshan.userservice.repositories.SessionRepository;
import dev.eshan.userservice.repositories.UserRepository;
import dev.eshan.userservice.services.interfaces.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRepository sessionRepository;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public UserDto signUp(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }

        User newUser = User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .firstName("")
                .lastName("")
                .phoneNumber("")
                .profileImageUrl("")
                .roles(new HashSet<>())
                .build();

        newUser = userRepository.save(newUser);
        return UserDto.from(newUser);
    }

    @Override
    public UserDto login(String email, String password, HttpServletResponse response) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        User user = userOptional.get();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Wrong Password");
        }

        SecretKey key = Jwts.SIG.HS256.key().build();
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles())
                .claim("exp", new Date(Instant.now().plusSeconds(3 * 24 * 60 * 60).toEpochMilli()))
                .signWith(key)
                .compact();

        UserDto userDto = UserDto.from(user);
        userDto.setToken(token);

        Session session = new Session();
        session.setExpiringAt(LocalDateTime.now().plusDays(3));
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        response.addCookie(new Cookie("auth-token", token));

        return userDto;
    }

    @Override
    public void logout(String token, String userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return;
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
    }

    @Override
    public SessionStatus validateToken(String token, String userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }
        Session session = sessionOptional.get();
        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return SessionStatus.ENDED;
        }

        Jws<Claims> claimsJws = Jwts.parser().build().parseSignedClaims(token);
        Date createdAt = (Date) claimsJws.getPayload().get("createdAt");

        if (createdAt.before(new Date())) {
            return SessionStatus.ENDED;
        }

        return SessionStatus.ACTIVE;
    }
}
