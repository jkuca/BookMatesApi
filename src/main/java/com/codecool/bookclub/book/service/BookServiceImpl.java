package com.codecool.bookclub.book.service;

import com.codecool.bookclub.book.model.Book;
import com.codecool.bookclub.book.model.BookDetails;
import com.codecool.bookclub.book.model.Shelf;
import com.codecool.bookclub.book.repository.BookDetailsRepository;
import com.codecool.bookclub.book.repository.BookPaginationRepository;
import com.codecool.bookclub.book.repository.BookRepository;
import com.codecool.bookclub.user.model.User;
import com.codecool.bookclub.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookDetailsRepository bookDetailsRepository;
    private final UserRepository userRepository;
    private final BookPaginationRepository paginationRepository;


    public BookServiceImpl(BookRepository bookRepository, BookDetailsRepository bookDetailsRepository, UserRepository userRepository, BookPaginationRepository paginationRepository) {
        this.bookRepository = bookRepository;
        this.bookDetailsRepository = bookDetailsRepository;
        this.userRepository = userRepository;
        this.paginationRepository = paginationRepository;
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Book> findAllBooks(Pageable paging) {
        return paginationRepository.findAll(paging);
    }


    @Override
    public List<Book> findTopFourBooks() {
        List<Book> books = bookRepository.findFirst4ByOrderByRatingDesc();
        return books;
    }

    /*TODO: check if the book is already in users books, optional if not in db => not in users books*/
    public void saveBookToShelf(Book book, Shelf shelf, Long userId) {
        User user = userRepository.getReferenceById(userId);
        Optional<Book> bookInDb = bookRepository.findByExternalId(book.getExternalId());
        Book savedBook = bookInDb.orElseGet(() -> bookRepository.save(book));
        if (bookDetailsRepository.findAllByUserIdAndBookId(user.getId(), savedBook.getId()).isEmpty()) {
            BookDetails bookDetails = BookDetails.builder()
                    .book(savedBook)
                    .shelf(shelf)
                    .user(user)
                    .build();
            bookDetailsRepository.save(bookDetails);
        }
    }



//    public int rateBook(BigDecimal userRating, long bookId) {
//        BigDecimal currentRating = bookRepository.findBookById(bookId).getRating();
//        int numberOfRatings = bookRepository.findBookById(bookId).getRatingsCount();
//        BigDecimal newRating = (currentRating * numberOfRatings + userRating) / (numberOfRatings + 1)
//
//    }


}
