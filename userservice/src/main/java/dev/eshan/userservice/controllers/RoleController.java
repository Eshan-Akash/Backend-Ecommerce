package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.CreateRoleRequestDto;
import dev.eshan.userservice.models.Role;
import dev.eshan.userservice.services.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/roles")
@Slf4j
public class RoleController {
    private RoleServiceImpl roleServiceImpl;

    public RoleController(RoleServiceImpl roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    @PostMapping
    public Role createRole(CreateRoleRequestDto request) {
        try {
            return roleServiceImpl.createRole(request.getRole());
        } catch (Exception e) {
            log.error("Error creating role", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating role");
        }
    }
}
