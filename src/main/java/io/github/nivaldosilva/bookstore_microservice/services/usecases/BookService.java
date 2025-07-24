package io.github.nivaldosilva.bookstore_microservice.services.usecases;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.nivaldosilva.bookstore_microservice.dtos.common.PagedResponse;
import io.github.nivaldosilva.bookstore_microservice.dtos.request.BookRequest;
import io.github.nivaldosilva.bookstore_microservice.dtos.response.BookResponse;
import io.github.nivaldosilva.bookstore_microservice.entities.Author;
import io.github.nivaldosilva.bookstore_microservice.entities.Book;
import io.github.nivaldosilva.bookstore_microservice.enums.Genre;
import io.github.nivaldosilva.bookstore_microservice.exceptions.AuthorNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.BookNotFoundException;
import io.github.nivaldosilva.bookstore_microservice.exceptions.IsbnAlreadyExistsException;
import io.github.nivaldosilva.bookstore_microservice.mappers.BookMapper;
import io.github.nivaldosilva.bookstore_microservice.repositories.AuthorRepository;
import io.github.nivaldosilva.bookstore_microservice.repositories.BookRepository;
import io.github.nivaldosilva.bookstore_microservice.services.interfaces.IBookService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new IsbnAlreadyExistsException();
        }
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(AuthorNotFoundException::new);

        Book book = BookMapper.toEntity(request, author);
        Book savedBook = bookRepository.save(book);
        return BookMapper.toResponse(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        return BookMapper.toResponse(book);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<BookResponse> findAllBooks(Pageable pageable, Genre genre, UUID authorId) {
        Specification<Book> spec = (root, query, cb) -> cb.conjunction();

        if (genre != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("genre"), genre));
        }
        if (authorId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("author").get("id"), authorId));
        }
        Page<Book> bookPage = bookRepository.findAll(spec, pageable);

        List<BookResponse> content = bookPage.getContent().stream()
                .map(BookMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                bookPage.getNumber(),
                bookPage.getSize(),
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.isFirst(),
                bookPage.isLast());
    }

    @Override
    @Transactional
    public BookResponse updateBook(UUID id, BookRequest request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

        if (!existingBook.getIsbn().equals(request.isbn()) && bookRepository.existsByIsbn(request.isbn())) {
            throw new IsbnAlreadyExistsException();
        }

        Author authorToUpdate = existingBook.getAuthor();
        if (!request.authorId().equals(existingBook.getAuthor().getId())) {
            authorToUpdate = authorRepository.findById(request.authorId())
                    .orElseThrow(AuthorNotFoundException::new);
        }

        existingBook.setIsbn(request.isbn());
        existingBook.setTitle(request.title());
        existingBook.setSynopsis(request.synopsis());
        existingBook.setGenre(request.genre());
        existingBook.setPublicationDate(request.publicationDate());
        existingBook.setPrice(request.price());
        existingBook.setStockQuantity(request.stockQuantity());
        existingBook.setAuthor(authorToUpdate);

        Book updatedBook = bookRepository.save(existingBook);
        return BookMapper.toResponse(updatedBook);
    }


    @Override
    @Transactional
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException();
        }
        bookRepository.deleteById(id);
    }          

}
