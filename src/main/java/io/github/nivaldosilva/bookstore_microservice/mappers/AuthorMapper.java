package io.github.nivaldosilva.bookstore_microservice.mappers;

import io.github.nivaldosilva.bookstore_microservice.dtos.request.AuthorRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.AuthorResponse;
import io.github.nivaldosilva.bookstore_microservice.entities.Author;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthorMapper {

    public static Author toEntity(AuthorRequest request) {
        return Author.builder()
                .id(request.id())
                .name(request.name())
                .nationality(request.nationality())
                .birthDate(request.birthDate())
                .biography(request.biography())
                .build();
    }

    public static AuthorResponse toResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .nationality(author.getNationality())
                .birthDate(author.getBirthDate())
                .biography(author.getBiography())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();

    }

}
