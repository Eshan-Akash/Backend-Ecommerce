package dev.eshan.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurity {
    /***
     * NOTE : We are removing this since we are using OAuth2 now, and we don't need this
     */
//    @Bean
//    public SecurityFilterChain filteringCriteria(HttpSecurity http) throws Exception {
//        http.csrf((csrf) -> csrf.disable());
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//        return http.build();
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
