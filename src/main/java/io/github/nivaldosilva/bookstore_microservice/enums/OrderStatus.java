package io.github.nivaldosilva.bookstore_microservice.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED

}
