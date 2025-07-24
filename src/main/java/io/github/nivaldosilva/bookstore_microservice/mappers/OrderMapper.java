package io.github.nivaldosilva.bookstore_microservice.mappers;

import java.util.List;
import java.util.stream.Collectors;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.OrderRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.OrderItemResponse;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore_microservice.entities.Customer;
import io.github.nivaldosilva.bookstore_microservice.entities.Order;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderMapper {

    public static Order toEntity(OrderRequest request, Customer customer) {
        return Order.builder()
                .customer(customer)
                .build();
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemDTOs = null;
        if (order.getItems() != null) {
            itemDTOs = order.getItems().stream()
                    .map(OrderItemMapper::toResponse)
                    .collect(Collectors.toList());
        }

        return OrderResponse.builder()
                .id(order.getId())
                .customerEmail((order.getCustomer() != null) ? order.getCustomer().getEmail() : null)
                .customerFullName((order.getCustomer() != null) ? order.getCustomer().getFullName() : null)
                .items(itemDTOs)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

}
