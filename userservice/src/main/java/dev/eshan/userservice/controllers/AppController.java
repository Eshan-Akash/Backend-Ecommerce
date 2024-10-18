package dev.eshan.userservice.controllers;

import dev.eshan.userservice.dtos.RegisterAppDto;
import dev.eshan.userservice.services.impl.AppServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app")
public class AppController {
    private final AppServiceImpl appServiceImpl;

    public AppController(AppServiceImpl appServiceImpl) {
        this.appServiceImpl = appServiceImpl;
    }

    @PostMapping("/register")
    public RegisterAppDto registerApp(@RequestBody RegisterAppDto appDto) {
        return appServiceImpl.registerApp(appDto);
    }

    @GetMapping("/clientId/{clientId}")
    public RegisterAppDto getRegisteredAppsByClientId(@PathVariable("clientId") String clientId) {
        return appServiceImpl.getRegisteredApp(clientId);
    }
}
