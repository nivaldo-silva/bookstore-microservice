package io.github.nivaldosilva.bookstore_microservice.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore_microservice.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    boolean existsByName(String name);

}
