package dev.eshan.productservice.repositories;

import dev.eshan.productservice.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, String> {

}
