package io.github.nivaldosilva.bookstore_microservice.services.interfaces;

import java.util.List;
import java.util.UUID;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.OrderRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore_microservice.enums.OrderStatus;

public interface IOrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse findOrderById(UUID id);

    List<OrderResponse> findAllOrders();

    OrderResponse updateOrderStatus(UUID id, OrderStatus newStatus);

    void deleteOrder(UUID id);
}
