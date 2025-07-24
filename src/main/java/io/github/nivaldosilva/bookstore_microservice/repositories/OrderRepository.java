package io.github.nivaldosilva.bookstore_microservice.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore_microservice.entities.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
