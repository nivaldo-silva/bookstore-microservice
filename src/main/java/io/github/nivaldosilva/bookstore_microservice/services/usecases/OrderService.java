package io.github.nivaldosilva.bookstore_microservice.services.usecases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nivaldosilva.bookstore_microservice.dtos.request.OrderItemRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.OrderRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore_microservice.entities.Book;
import io.github.nivaldosilva.bookstore_microservice.entities.Customer;
import io.github.nivaldosilva.bookstore_microservice.entities.Order;
import io.github.nivaldosilva.bookstore_microservice.entities.OrderItem;
import io.github.nivaldosilva.bookstore_microservice.enums.OrderStatus;
import io.github.nivaldosilva.bookstore_microservice.exceptions.BookNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.CustomerNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.InsufficientStockException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.OrderNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.mappers.OrderMapper;
import io.github.nivaldosilva.bookstore_microservice.repositories.BookRepository;
import io.github.nivaldosilva.bookstore_microservice.repositories.CustomerRepository;
import io.github.nivaldosilva.bookstore_microservice.repositories.OrderRepository;
import io.github.nivaldosilva.bookstore_microservice.services.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findByEmail(orderRequest.customerEmail())
                .orElseThrow(CustomerNotFoundException::new);

        if (orderRequest.items() == null || orderRequest.items().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item.");
        }

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : orderRequest.items()) {
            Book book = bookRepository.findByIsbn(itemRequest.bookIsbn())
                    .orElseThrow(BookNotFoundException::new);

            if (book.getStockQuantity() < itemRequest.quantity()) {
                throw new InsufficientStockException(
                        "Estoque insuficiente para o livro: " + book.getTitle() + ". Disponível: "
                                + book.getStockQuantity() + ", Solicitado: " + itemRequest.quantity());
            }

            BigDecimal unitPrice = book.getPrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(itemRequest.quantity()));

            book.setStockQuantity(book.getStockQuantity() - itemRequest.quantity());
            bookRepository.save(book);

            OrderItem orderItem = OrderItem.builder()
                    .book(book)
                    .quantity(itemRequest.quantity())
                    .unitPrice(unitPrice)
                    .totalPrice(totalPrice)
                    .order(order)
                    .build();
            orderItems.add(orderItem);

            totalOrderAmount = totalOrderAmount.add(totalPrice);
        }

        order.setTotalAmount(totalOrderAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UUID id, OrderStatus newStatus) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        existingOrder.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(existingOrder);

        return OrderMapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
        orderRepository.deleteById(id);
    }

}
