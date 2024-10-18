package dev.eshan.userservice.repositories;

import dev.eshan.userservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {


    List<Role> findAllByIdIn(List<Long> roleIds);
}
