package io.github.nivaldosilva.bookstore_microservice.services.usecases;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.AuthorRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.AuthorResponse;
import io.github.nivaldosilva.bookstore_microservice.entities.Author;
import io.github.nivaldosilva.bookstore_microservice.exceptions.AuthorNameAlreadyExistsException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.AuthorNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.mappers.AuthorMapper;
import io.github.nivaldosilva.bookstore_microservice.repositories.AuthorRepository;
import io.github.nivaldosilva.bookstore_microservice.services.interfaces.IAuthorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService implements IAuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (authorRepository.existsByName(request.name())) {
            throw new AuthorNameAlreadyExistsException();
        }
        Author author = AuthorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return AuthorMapper.toResponse(savedAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse findAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(AuthorNotFoundException::new);
        return AuthorMapper.toResponse(author);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> findAllAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuthorResponse updateAuthor(UUID id, AuthorRequest request) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(AuthorNotFoundException::new);

        if (!existingAuthor.getName().equals(request.name()) && authorRepository.existsByName(request.name())) {
            throw new AuthorNameAlreadyExistsException();
        }

        existingAuthor.setName(request.name());
        existingAuthor.setNationality(request.nationality());
        existingAuthor.setBirthDate(request.birthDate());
        existingAuthor.setBiography(request.biography());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        return AuthorMapper.toResponse(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(UUID id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException();
        }
        authorRepository.deleteById(id);
    }

}
