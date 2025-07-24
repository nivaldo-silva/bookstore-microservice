package io.github.nivaldosilva.bookstore_microservice.mappers;

import io.github.nivaldosilva.bookstore_microservice.dtos.request.OrderItemRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.OrderItemResponse;
import io.github.nivaldosilva.bookstore_microservice.entities.Book;
import io.github.nivaldosilva.bookstore_microservice.entities.Order;
import io.github.nivaldosilva.bookstore_microservice.entities.OrderItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderItemMapper {

        public static OrderItem toEntity(OrderItemRequest request, Order order, Book book) {
                return OrderItem.builder()
                                .order(order)
                                .book(book)
                                .quantity(request.quantity())
                                .build();
        }

        public static OrderItemResponse toResponse(OrderItem orderItem) {
                String bookAuthorName = null;
                if (orderItem.getBook() != null && orderItem.getBook().getAuthor() != null) {
                        bookAuthorName = orderItem.getBook().getAuthor().getName();
                }
                return OrderItemResponse.builder()
                                .id(orderItem.getId())
                                .bookId((orderItem.getBook() != null) ? orderItem.getBook().getId() : null)
                                .bookIsbn((orderItem.getBook() != null) ? orderItem.getBook().getIsbn() : null)
                                .bookTitle((orderItem.getBook() != null) ? orderItem.getBook().getTitle() : null)
                                .bookAuthorName(bookAuthorName)
                                .quantity(orderItem.getQuantity())
                                .unitPrice(orderItem.getUnitPrice())
                                .totalPrice(orderItem.getTotalPrice())
                                .build();
        }

}
