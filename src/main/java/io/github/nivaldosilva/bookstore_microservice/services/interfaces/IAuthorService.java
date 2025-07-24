package io.github.nivaldosilva.bookstore_microservice.services.interfaces;

import java.util.List;
import java.util.UUID;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.AuthorRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.AuthorResponse;

public interface IAuthorService {

    AuthorResponse createAuthor(AuthorRequest request);

    AuthorResponse findAuthorById(UUID id);

    List<AuthorResponse> findAllAuthors();

    AuthorResponse updateAuthor(UUID id, AuthorRequest request);

    void deleteAuthor(UUID id);

}
