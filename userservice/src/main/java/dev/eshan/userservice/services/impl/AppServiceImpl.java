package dev.eshan.userservice.services.impl;

import dev.eshan.userservice.dtos.RegisterAppDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class AppServiceImpl {
    private final RegisteredClientRepository registeredClientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppServiceImpl(RegisteredClientRepository registeredClientRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.registeredClientRepository = registeredClientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public RegisterAppDto registerApp(@RequestBody RegisterAppDto appDto) {
        String clientId = UUID.randomUUID().toString();
        RegisteredClient oidcClient = RegisteredClient
                .withId(appDto.getId())
                .clientId(clientId)
                .clientSecret(bCryptPasswordEncoder.encode(appDto.getClientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri(String.join(",", appDto.getRedirectUris()))
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        registeredClientRepository.save(oidcClient);
        return RegisterAppDto
                .builder()
                .id(oidcClient.getId())
                .clientId(clientId)
                .clientSecret("*****")
                .authorizationGrantTypes(oidcClient.getAuthorizationGrantTypes())
                .clientAuthenticationMethods(oidcClient.getClientAuthenticationMethods())
                .redirectUris(oidcClient.getRedirectUris())
                .postLogoutRedirectUris(oidcClient.getPostLogoutRedirectUris())
                .scopes(oidcClient.getScopes())
                .clientSettings(oidcClient.getClientSettings())
                .build();
    }

    public RegisterAppDto getRegisteredApp(String clientId) {
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        return RegisterAppDto
                .builder()
                .id(registeredClient.getId())
                .clientId(registeredClient.getClientId())
                .clientSecret("*****")
                .authorizationGrantTypes(registeredClient.getAuthorizationGrantTypes())
                .clientAuthenticationMethods(registeredClient.getClientAuthenticationMethods())
                .redirectUris(registeredClient.getRedirectUris())
                .postLogoutRedirectUris(registeredClient.getPostLogoutRedirectUris())
                .scopes(registeredClient.getScopes())
                .clientSettings(registeredClient.getClientSettings())
                .build();
    }
}
