package dev.eshan.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@MappedSuperclass
public class BaseModel {
    //    @Id
//    @GeneratedValue(generator = "uuidgenerator")
//    @GenericGenerator(name = "uuidgenerator", strategy = "uuid2")
//    @Column(name = "id", columnDefinition = "binary(16)", nullable = false, updatable = false)
//    private UUID uuid;
    @Id
    @GeneratedValue(generator = "uuidgenerator")
    @GenericGenerator(name = "uuidgenerator", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "varchar(64)", nullable = false, updatable = false)
    private String id;
}
