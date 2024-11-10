package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.RegisterAppDto;
import dev.eshan.userservice.services.impl.AppServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/app")
@Slf4j
public class AppController {
    private final AppServiceImpl appServiceImpl;

    public AppController(AppServiceImpl appServiceImpl) {
        this.appServiceImpl = appServiceImpl;
    }

    @PostMapping("/register")
    public RegisterAppDto registerApp(@RequestBody RegisterAppDto appDto) {
        try {
            return appServiceImpl.registerApp(appDto);
        } catch (Exception e) {
            log.error("Error registering app", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error registering app");
        }
    }

    @GetMapping("/clientId/{clientId}")
    public RegisterAppDto getRegisteredAppsByClientId(@PathVariable("clientId") String clientId) {
        try {
            return appServiceImpl.getRegisteredApp(clientId);
        } catch (Exception e) {
            log.error("Error fetching app", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching app");
        }
    }
}
