package io.github.nivaldosilva.bookstore_microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IsbnAlreadyExistsException extends RuntimeException {
    public IsbnAlreadyExistsException() {
        super("Livro com este ISBN já existe.");
    }
}
