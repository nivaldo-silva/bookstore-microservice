package io.github.nivaldosilva.bookstore_microservice.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.bookstore_microservice.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

}
