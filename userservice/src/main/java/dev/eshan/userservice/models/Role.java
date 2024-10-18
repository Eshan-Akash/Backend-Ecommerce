package dev.eshan.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonDeserialize(as = Role.class)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseModel {
    @Column(nullable = false, unique = true)
    private String role;
}
