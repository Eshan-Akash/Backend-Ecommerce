package dev.eshan.userservice.repositories;

import dev.eshan.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    Optional<Session> findByTokenAndUser_Id(String token, String userId);
}