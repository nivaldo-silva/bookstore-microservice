package io.github.nivaldosilva.bookstore_microservice.services.interfaces;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import io.github.nivaldosilva.bookstore_microservice.dtos.common.PagedResponse;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.BookRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.BookResponse;
import io.github.nivaldosilva.bookstore_microservice.enums.Genre;

public interface IBookService {

    BookResponse createBook(BookRequest request);

    BookResponse findBookById(UUID id);

    PagedResponse<BookResponse> findAllBooks(Pageable pageable, Genre genre, UUID authorId);

    BookResponse updateBook(UUID id, BookRequest request);

    void deleteBook(UUID id);

}
