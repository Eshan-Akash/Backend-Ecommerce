package dev.eshan.userservice.services.impl;

import dev.eshan.userservice.models.Role;
import dev.eshan.userservice.repositories.RoleRepository;
import dev.eshan.userservice.services.interfaces.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Override
    public Role createRole(String name) {
        return null;
    }
}
