package dev.eshan.userservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Data
public class BaseModel {
    @Id
    @GeneratedValue(generator = "uuidgenerator")
    @GenericGenerator(name = "uuidgenerator", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "varchar(64)", nullable = false, updatable = false)
    private String id;
}
