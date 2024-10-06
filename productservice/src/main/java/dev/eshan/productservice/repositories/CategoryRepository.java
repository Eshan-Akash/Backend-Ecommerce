package dev.eshan.productservice.repositories;

import dev.eshan.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findById(String uuid);

    @Override
    List<Category> findAllById(Iterable<String> uuids);
}
