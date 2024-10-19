package dev.eshan.userservice.security.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthUserDetails extends User implements UserDetails {

    private final List<String> permissions;
    private String role;

    public AuthUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                           List<String> permissions) {
        super(username, password, authorities);
        this.permissions = permissions;
        if (!authorities.isEmpty()) {
            this.role = authorities.iterator().next().getAuthority();
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (permissions == null) {
            return authorities;
        }
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    public String getRole() {
        return role;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}