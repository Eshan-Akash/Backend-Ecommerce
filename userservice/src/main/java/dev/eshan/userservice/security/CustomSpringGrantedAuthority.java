package dev.eshan.userservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.eshan.userservice.models.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@JsonDeserialize(as = CustomSpringGrantedAuthority.class)
@NoArgsConstructor
public class CustomSpringGrantedAuthority implements GrantedAuthority {
    private Role role;
    public CustomSpringGrantedAuthority(Role role) {
        this.role = role;
    }
    @Override
    @JsonIgnore
    public String getAuthority() {
        return role.getRole();
    }
}
