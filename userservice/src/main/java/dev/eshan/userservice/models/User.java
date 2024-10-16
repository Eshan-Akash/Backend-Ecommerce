package dev.eshan.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@JsonDeserialize(as = User.class)
public class User extends BaseModel {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String profileImageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Boolean isEnabled = true;

    // One-to-Many Relationship with Roles (Each User can have multiple Roles)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // If you are using two-factor authentication
    private Boolean twoFactorEnabled = false;
}