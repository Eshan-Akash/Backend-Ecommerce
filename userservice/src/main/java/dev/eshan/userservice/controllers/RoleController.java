package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.CreateRoleRequestDto;
import dev.eshan.userservice.models.Role;
import dev.eshan.userservice.services.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {
    private RoleServiceImpl roleServiceImpl;

    public RoleController(RoleServiceImpl roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    @PostMapping
    public Role createRole(CreateRoleRequestDto request) {
        return roleServiceImpl.createRole(request.getRole());
    }
}
