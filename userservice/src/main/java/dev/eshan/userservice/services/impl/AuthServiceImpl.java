package dev.eshan.userservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.eshan.userservice.configs.KafkaProducerClient;
import dev.eshan.userservice.dtos.SendEmailMessageDto;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRepository sessionRepository;
    private final KafkaProducerClient kafkaProducerClient;
    private final ObjectMapper objectMapper;

    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                           SessionRepository sessionRepository, KafkaProducerClient kafkaProducerClient, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
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
        UserDto userDto = UserDto.from(newUser);
        sendSignUpEmail(userDto);
        return userDto;
    }

    private void sendSignUpEmail(UserDto userDto) {
        try {
            String subject = "Welcome to ES EcommX - Your Journey Begins!";
            String body = "Hi [User's First Name],\n\n" +
                    "Welcome to Your ES EcommX!\n\n" +
                    "We’re thrilled to have you on board. Your account has been successfully created, and you are now part of a community committed to delivering the most seamless and enjoyable online shopping experience.\n\n" +
                    "Here’s what you can do next:\n" +
                    "- Explore our products/services: Check out what we have to offer and find what you need.\n" +
                    "- Complete your profile: Personalize your account to get a more tailored experience.\n" +
                    "- Stay updated: Be the first to know about our latest features, updates, and exclusive offers.\n\n" +
                    "If you have any questions or need assistance, our support team is here to help!\n\n" +
                    "Enjoy your experience with us,\n" +
                    "The ES EcommX Team\n\n";

            SendEmailMessageDto sendEmailMessageDto = new SendEmailMessageDto();
            sendEmailMessageDto.setTo(userDto.getEmail());
            sendEmailMessageDto.setSubject(subject);
            sendEmailMessageDto.setBody(body);
            kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(sendEmailMessageDto));
        } catch (Exception e) {
            log.error("Error while sending email: {}", e.getMessage());
        }
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
