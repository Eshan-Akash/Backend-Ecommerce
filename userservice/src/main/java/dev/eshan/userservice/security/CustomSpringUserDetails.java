package dev.eshan.userservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.eshan.userservice.models.Role;
import dev.eshan.userservice.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@JsonDeserialize(as = CustomSpringUserDetails.class)
@NoArgsConstructor
public class CustomSpringUserDetails implements UserDetails {
    private User user;
    public CustomSpringUserDetails(User user) {
        this.user = user;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<CustomSpringGrantedAuthority> customSpringGrantedAuthorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            customSpringGrantedAuthorities.add(
                    new CustomSpringGrantedAuthority(role)
            );
        }

        return customSpringGrantedAuthorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}